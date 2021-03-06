package mypackage;

import data.Account;
import data.DataService;
import data.DatabaseService;
import global.BalanceService;
import global.Locks;
import org.web3j.utils.Convert;
import periodic.PeriodicCollectManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Hello extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(Hello.class.getName());

    private final MessageGenerator _messageGenerator = MessageGenerator.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;
    private final PeriodicCollectManager _periodicCollectManager = PeriodicCollectManager.INSTANCE;
    private final BalanceService _balanceService = BalanceService.INSTANCE;
    private final CookieService _cookieService = CookieService.INSTANCE;
    private final Locks _locks = Locks.INSTANCE;

    @Override
    public void init() {
        _periodicCollectManager.startPeriodicCollectTask();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String walletAddress = req.getHeader(HttpUtil.WALLET_HEADER);
        if (walletAddress == null) {
            LOG.info("Received doGet request without wallet. Ignoring.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        synchronized (_locks.getLockForWallet(walletAddress)) {
            LOG.info("Received doGet request for wallet: " + walletAddress);
            String signature = req.getHeader(HttpUtil.SIGNATURE_HEADER);
            if (_messageGenerator.isSignatureInvalid(walletAddress, signature)) {
                LOG.info("Signature of request is not valid. Ignoring");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            boolean generateNewCookie = true;
            String existingCookie = _cookieService.getCookieForWallet(walletAddress);
            String cookie = HttpUtil.getCookie(req, existingCookie);
            if (existingCookie != null && cookie != null) {
                if (existingCookie.equals(cookie)) {
                    generateNewCookie = false;
                    LOG.info("Cookies for hello request for " + walletAddress + " match.");
                }
            }

            Account account = _dataservice.getAccount(walletAddress);
            if (account == null) {
                LOG.info("Didn't find account for " + walletAddress + ". Generating a new one.");
                account = Account.generateNew(walletAddress);
                if (account != null) {
                    if (!_dataservice.addAccount(account)) {
                        LOG.severe("Couldn't add newly generated account ot the DB.");
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }
                } else {
                    LOG.severe("Couldn't generate account for " + walletAddress);
                }
            } else {
                LOG.info("Found an existing account for " + walletAddress);
            }

            if (account == null) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            String cookieForSet;
            if (generateNewCookie) {
                cookieForSet = _cookieService.generateNewCookieForWallet(walletAddress);
            } else {
                cookieForSet = _cookieService.getCookieForWallet(walletAddress);
            }
            Cookie c = new Cookie(HttpUtil.ONLYONE_COOKIE, cookieForSet);
            c.setPath("/");
            // c.setHttpOnly(true);
            // c.setSecure(true); TODO SET THESE IN PRODUCTION
            resp.addCookie(c);
            LOG.info("Adding cookie " + c.getName() + ":" + c.getValue() + " to the response.");
            String oldToken = _cookieService.getTokenForWallet(walletAddress);
            if (oldToken != null) {
                LOG.info("Replacing token for wallet: " + walletAddress);
            }
            String token = _cookieService.generateNewTokenForWallet(walletAddress);

            _balanceService.updateBalance(account, resp);

            BigInteger web3OnlyoneBalance;
            try {
                web3OnlyoneBalance = Web3Service.INSTANCE.getOnlyone().balanceOf(walletAddress).send();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Cannot get web3OnlyoneBalance", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            BigDecimal web3OnlyoneBalanceDec = Convert.fromWei(web3OnlyoneBalance.toString(), Convert.Unit.ETHER);

            HttpUtil.postResponse(resp, new HelloResponse(
                    account.depositWalletAddress,
                    account.bnbBalance,
                    account.onlyoneBalance,
                    token,
                    web3OnlyoneBalanceDec));
        }
    }

    @Override
    public void destroy() {
        LOG.info("Destroying hello servlet");
        _periodicCollectManager.stopPeriodicCollectTask();
        DatabaseService.INSTANCE.closeSqlConnection();
    }

    static class HelloResponse {
        public String depositWalletAddress;
        public BigDecimal bnbBalance;
        public BigDecimal onlyoneBalance;
        public String token;
        public BigDecimal web3OnlyoneBalance;

        HelloResponse(
                String depositWalletAddress,
                BigDecimal bnbBalance,
                BigDecimal onlyoneBalance,
                String token,
                BigDecimal web3OnlyoneBalance) {
            this.depositWalletAddress = depositWalletAddress;
            this.bnbBalance = bnbBalance;
            this.onlyoneBalance = onlyoneBalance;
            this.token = token;
            this.web3OnlyoneBalance = web3OnlyoneBalance;
        }
    }
}

