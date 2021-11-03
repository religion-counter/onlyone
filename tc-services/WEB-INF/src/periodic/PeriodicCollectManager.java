package periodic;

import data.Account;
import data.DatabaseService;
import global.Constants;
import global.Locks;
import global.WalletUtil;
import mypackage.Web3Service;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeriodicCollectManager {

    private static final Logger LOG = Logger.getLogger(PeriodicCollectManager.class.getName());

    public static final PeriodicCollectManager INSTANCE = new PeriodicCollectManager();

    private static final long INTERVAL = 1;
    private static final TimeUnit INTERVAL_UNIT = TimeUnit.HOURS;

    private static final double COLLECT_THRESHOLD = 0.01;

    private ScheduledExecutorService _scheduledExecutor = null;
    private final DatabaseService _databaseService = DatabaseService.INSTANCE;
    private final Web3Service _web3service = Web3Service.INSTANCE;
    private final Locks _locks = Locks.INSTANCE;

    private PeriodicCollectManager() {}

    public void startPeriodicCollectTask() {
        LOG.info("Starting periodic collect task.");
        _scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        // Transfer the depositBnbBalance
        final PeriodicCollectTask periodicCollectTask = new PeriodicCollectTask();
        long intervalForSchedule = INTERVAL;
        TimeUnit unitForSchedule = INTERVAL_UNIT;
        LOG.info("Starting periodic collect task with " + intervalForSchedule +
                " interval and " + unitForSchedule + " unit.");
        _scheduledExecutor.schedule(periodicCollectTask, intervalForSchedule, unitForSchedule);
    }

    public void stopPeriodicCollectTask() {
        LOG.info("Stopping periodic collect task.");
        if (_scheduledExecutor != null) {

            List<Runnable> notExecutedRunnables = _scheduledExecutor.shutdownNow();
            if (notExecutedRunnables.size() == 0) {
                LOG.info("No scheduled collect tasks.");
            }

            for (Runnable runnable : notExecutedRunnables) {
                LOG.info("Skipping execution of " + runnable);
            }

            _scheduledExecutor = null;
        }
    }

    private class PeriodicCollectTask implements Runnable {

        long lastScheduleTime;

        PeriodicCollectTask() {
            lastScheduleTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            LOG.info("Running periodic collect task");
            try {
                List<Account> accountList = _databaseService.getAllAccounts();
                if (accountList == null) {
                    LOG.log(Level.SEVERE, "Couldn't get accounts list.");
                    return;
                }

                Credentials masterWallet = WalletUtil.getMasterWallet();
                if (masterWallet == null) {
                    LOG.log(Level.SEVERE, "Master wallet is null.");
                    return;
                }
                for (Account account : accountList) {
                    // Collect money if amount is greater than two times the withdraw tax.
                    if (account.depositBnbBalance >= COLLECT_THRESHOLD) {
                        try {
                            Credentials fromWallet = Credentials.create(account.depositWalletPk);
                            double amount = account.depositBnbBalance - Constants.WITHDRAW_TAX;
                            String txHash = _web3service.sendFunds(
                                    fromWallet,
                                    masterWallet.getAddress(),
                                    amount);
                            LOG.info("Sending " + amount + " from " + fromWallet.getAddress() + " to " +
                                    masterWallet.getAddress() + ".\nTxHash: " + txHash);
                            String newBalance = _web3service.getBalanceWei(account.depositWalletAddress);
                            BigDecimal newBalanceEth = Convert.fromWei(newBalance, Convert.Unit.ETHER);
                            account.depositBnbBalance = newBalanceEth.doubleValue();
                            synchronized (_locks.getLockForWallet(account.walletAddress)) {
                                if (!_databaseService.updateAccount(account)) {
                                    LOG.log(Level.SEVERE, "Couldn't update account with new balance");
                                }
                            }
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, "Couldn't send money from: " + account.walletAddress);
                        }
                    }
                }
            } finally {
                long intervalForSchedule = INTERVAL;
                TimeUnit unitForSchedule = INTERVAL_UNIT;
                LOG.info("Rescheduling collect task for " + intervalForSchedule +
                        " interval and " + unitForSchedule + " unit");
                if (_scheduledExecutor != null) {
                    lastScheduleTime = System.currentTimeMillis();
                    _scheduledExecutor.schedule(this, intervalForSchedule, unitForSchedule);
                }
            }
        }

        @Override
        public String toString() {
            return "PeriodicCollectTask scheduled for execution at: " + new Date(lastScheduleTime);
        }
    }
}
