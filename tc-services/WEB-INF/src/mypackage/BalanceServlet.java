package mypackage;

import data.Account;
import data.DataService;
import global.BalanceService;
import global.Locks;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class BalanceServlet extends HttpServlet {

    private final Logger LOG = Logger.getLogger(BalanceServlet.class.getName());

    private final CookieService _cookieService = CookieService.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;
    private final BalanceService _balanceService = BalanceService.INSTANCE;
    private final Locks _locks = Locks.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (_cookieService.isRequestUnauthenticated(req)) {
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
        synchronized (_locks.getLockForWallet(wallet)) {
            Account acc = _dataservice.getAccount(wallet);
            if (acc == null) {
                LOG.severe("Couldn't get account for wallet");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            _balanceService.updateBalance(acc, resp);
            HttpUtil.postResponse(resp, acc.bnbBalance.toString());
        }
    }
}
