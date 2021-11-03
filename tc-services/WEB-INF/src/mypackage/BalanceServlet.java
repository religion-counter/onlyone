package mypackage;

import data.Account;
import data.DataService;
import global.BalanceService;
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
    private final BalanceService _balanceService = BalanceService.INSTANCE;

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

            _balanceService.updateBalance(acc, resp);
            HttpUtil.postResponse(resp, Double.toString(acc.bnbBalance));
        }
    }
}
