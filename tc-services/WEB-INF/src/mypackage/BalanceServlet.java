package mypackage;

import data.Account;
import data.DataService;
import global.GlobalApplicationLock;
import org.web3j.utils.Convert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class BalanceServlet extends HttpServlet {

    private final Logger LOG = Logger.getLogger(BalanceServlet.class.getName());

    private final CookieService _cookieService = CookieService.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;
    private final Web3Service _web3service = Web3Service.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (GlobalApplicationLock.INSTANCE) {
            if (!_cookieService.isRequestAuthenticated(req)) {
                LOG.info("Request is not authenticated");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String wallet = req.getHeader(HttpUtil.WALLET_HEADER);
            if (wallet == null) {
                LOG.severe("Checking balance for null wallet.");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            Account acc = _dataservice.getAccount(wallet);
            if (acc == null) {
                LOG.severe("Couldn't get account for wallet");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            double web3DepositBalance = getWeb3Balance(acc.depositWalletAddress);
            if (web3DepositBalance < acc.depositBnbBalance) {
                LOG.severe("Web3 balance is less than balance in DB.");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (web3DepositBalance > acc.depositBnbBalance) {
                LOG.info("Adding balance: " + (web3DepositBalance - acc.depositBnbBalance) +
                        ". Web3 balance: " + web3DepositBalance +
                        ", Balance in DB: " + acc.depositBnbBalance + ". ");
                acc.bnbBalance += web3DepositBalance - acc.depositBnbBalance;
                acc.depositBnbBalance = web3DepositBalance;
                if (!_dataservice.updateAccount(acc)) {
                    LOG.severe("Couldn't update account: " + wallet);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
            }
            HttpUtil.postResponse(resp, Double.toString(acc.bnbBalance));
        }
    }

    public double getWeb3Balance(String wallet) {
        synchronized (GlobalApplicationLock.INSTANCE) {
            String wei = _web3service.getBalanceWei(wallet);
            return Convert.fromWei(wei, Convert.Unit.ETHER).doubleValue();
        }
    }
}
