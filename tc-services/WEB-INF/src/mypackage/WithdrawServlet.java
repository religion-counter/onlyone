package mypackage;

import data.Account;
import data.DataService;
import global.Constants;
import global.GlobalApplicationLock;
import global.WalletUtil;
import org.web3j.crypto.Credentials;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WithdrawServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(WithdrawServlet.class.getName());

    private final Web3Service _web3service = Web3Service.INSTANCE;
    private final CookieService _cookieService = CookieService.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        synchronized (GlobalApplicationLock.INSTANCE) {
            String wallet = req.getHeader(HttpUtil.WALLET_HEADER);
            if (wallet == null) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                LOG.log(Level.SEVERE, "Empty wallet header");
                return;
            }
            if (!_cookieService.isRequestAuthenticated(req)) {
                LOG.info("Request is not authenticated");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Account acc = _dataservice.getAccount(wallet);
            if (acc == null) {
                LOG.severe("Couldn't get account for wallet");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            String amount = req.getHeader(HttpUtil.AMOUNT_HEADER);
            if (amount == null) {
                LOG.severe("Invalid AMOUNT header");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            double amountToWithdraw;
            try {
                amountToWithdraw = Double.parseDouble(amount);
            } catch (Exception e) {
                LOG.severe("Invalid AMOUNT header: " + amount);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (amountToWithdraw > acc.bnbBalance) {
                LOG.severe("Trying to withdraw more than BNB balance. Amount to withdraw: " +
                        amountToWithdraw + ", BNB balance: " + acc.bnbBalance);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (amountToWithdraw < Constants.WITHDRAW_TAX) {
                LOG.severe("Trying to withdraw less than BNB tax. Amount to withdraw: " + amountToWithdraw);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            acc.bnbBalance -= amountToWithdraw;
            amountToWithdraw -= Constants.WITHDRAW_TAX;

            String txHash = withdrawTo(acc.walletAddress, amountToWithdraw);
            if (txHash == null) {
                LOG.severe("Couldn't withdraw: " + amountToWithdraw + " to wallet: " + wallet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            if (!_dataservice.updateAccount(acc)) {
                LOG.severe("Couldn't update account after withdraw. FATAL Crashing the server");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                System.exit(1);
                return;
            }

            HttpUtil.postResponse(resp, txHash);
        }
    }

    /**
     * Returns transaction hash.
     */
    private String withdrawTo(String walletAddress, double amountToWithdraw) {
        // read master wallet and pk from /etc/onlyone/acc
        try {
            Credentials masterWallet = WalletUtil.getMasterWallet();

            String txHash = _web3service.sendFunds(masterWallet, walletAddress, amountToWithdraw);
            LOG.info("Transaction for send: " + txHash);
            return txHash;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Couldn't withdraw: ", e);
            return null;
        }
    }
}
