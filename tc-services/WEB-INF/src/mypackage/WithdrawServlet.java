package mypackage;

import data.Account;
import data.DataService;
import data.DatabaseService;
import org.web3j.tx.Transfer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WithdrawServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(WithdrawServlet.class.getName());

    private static final Double WITHDRAW_TAX = 0.005;

    private final Web3Service _web3service = Web3Service.INSTANCE;
    private final CookieService _cookieService = CookieService.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        Double amountToWithdraw = 0D;
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
        if (amountToWithdraw < WITHDRAW_TAX) {
            LOG.severe("Trying to withdraw less than BNB tax. Amount to withdraw: " + amountToWithdraw);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        acc.bnbBalance -= amountToWithdraw;
        amountToWithdraw -= WITHDRAW_TAX;

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

    /**
     * Returns transaction hash.
     */
    private synchronized String withdrawTo(String walletAddress, double amountToWithdraw) {
        // read master wallet and pk from /etc/onlyone/acc
        try {
            File f = new File("/etc/onlyone/acc");
            BufferedReader is = new BufferedReader(new FileReader(f));
            String masterAddr = is.readLine();
            String masterPk = is.readLine();

            String txHash = _web3service.sendFunds(masterAddr, masterPk, walletAddress, amountToWithdraw);

            return txHash;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Couldn't withdraw: ", e);
            return null;
        }
    }
}
