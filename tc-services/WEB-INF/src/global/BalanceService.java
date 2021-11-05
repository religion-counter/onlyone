package global;

import data.Account;
import data.DataService;
import mypackage.Web3Service;
import org.web3j.utils.Convert;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.logging.Logger;

public class BalanceService {

    private final static Logger LOG = Logger.getLogger(BalanceService.class.getName());

    public final static BalanceService INSTANCE = new BalanceService();

    private final Locks _locks = Locks.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;
    private final Web3Service _web3service = Web3Service.INSTANCE;

    public void updateBalance(Account acc, HttpServletResponse response) {
        BigDecimal web3DepositBalance = getWeb3Balance(acc.depositWalletAddress);
        if (web3DepositBalance.compareTo(acc.depositBnbBalance) < 0) {
            LOG.severe("Web3 balance is less than balance in DB.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } else if (web3DepositBalance.compareTo(acc.depositBnbBalance) > 0) {
            LOG.info("Adding balance: " + web3DepositBalance.subtract(acc.depositBnbBalance) +
                    ". Web3 balance: " + web3DepositBalance +
                    ", Balance in DB: " + acc.depositBnbBalance + ". ");
            acc.bnbBalance = acc.bnbBalance.add(web3DepositBalance.subtract(acc.depositBnbBalance));
            acc.depositBnbBalance = web3DepositBalance;
            if (!_dataservice.updateAccount(acc)) {
                LOG.severe("Couldn't update account: " + acc.walletAddress);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }
        LOG.info("Updated balance successfully for " + acc.walletAddress);
        // TODO Implement update for Onlyone token as well.
    }

    public BigDecimal getWeb3Balance(String wallet) {
        synchronized (_locks.getLockForWallet(wallet)) {
            String wei = _web3service.getBalanceWei(wallet);
            return Convert.fromWei(wei, Convert.Unit.ETHER);
        }
    }
}
