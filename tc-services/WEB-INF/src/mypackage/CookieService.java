package mypackage;

import global.GlobalApplicationLock;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CookieService {

    private static final Logger LOG = Logger.getLogger(CookieService.class.getName());

    public static final CookieService INSTANCE = new CookieService();

    private SecureRandom random = new SecureRandom();

    private CookieService() {}

    private final ConcurrentHashMap<String, String> cookieToWallet = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> tokensForWallet = new ConcurrentHashMap<>();

    public String getCookieForWallet(String wallet) {
        synchronized (GlobalApplicationLock.INSTANCE) {
            if (cookieToWallet.containsKey(wallet)) {
                return cookieToWallet.get(wallet);
            }
            return null;
        }
    }

    public String getTokenForWallet(String wallet) {
        synchronized (GlobalApplicationLock.INSTANCE) {
            if (tokensForWallet.containsKey(wallet)) {
                return tokensForWallet.get(wallet);
            }
            return null;
        }
    }

    public String generateNewTokenForWallet(String wallet) {
        synchronized (GlobalApplicationLock.INSTANCE) {
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < 24; ++i) {
                token.append((char) ('a' + random.nextInt(25)));
            }
            String tokenStr = token.toString();
            tokensForWallet.put(wallet, tokenStr);
            LOG.info("Generated new token for " + wallet);
            return tokenStr;
        }
    }

    public String generateNewCookieForWallet(String wallet) {
        synchronized (GlobalApplicationLock.INSTANCE) {
            StringBuilder cookie = new StringBuilder();
            for (int i = 0; i < 26; ++i) {
                cookie.append((char) ('a' + random.nextInt(25)));
            }
            String cookieStr = cookie.toString();
            cookieToWallet.put(wallet, cookieStr);
            LOG.info("Generated new cookie: " +
                    HttpUtil.ONLYONE_COOKIE + ":" + cookieStr + " for " + wallet);
            return cookieStr;
        }
    }

    public boolean isRequestAuthenticated(HttpServletRequest req) {
        synchronized (GlobalApplicationLock.INSTANCE) {
            if (req == null) return false;
            String walletAddress = req.getHeader(HttpUtil.WALLET_HEADER);
            if (walletAddress == null) {
                LOG.info("Request is without a wallet in the header.");
                return false;
            }

            String tokenFromReq = req.getHeader(HttpUtil.TOKEN_HEADER);
            if (tokenFromReq == null) {
                LOG.info("Token header is missing.");
                return false;
            }
            String requiredToken = getTokenForWallet(walletAddress);
            if (requiredToken == null) {
                LOG.info("Token for wallet is missing.");
                return false;
            }
            if (!tokenFromReq.equals(requiredToken)) {
                LOG.info("Token from request doesn't match required token. " +
                        "Token from request: " + tokenFromReq +
                        ". Token for wallet: " + requiredToken);
                return false;
            }
            String requiredCookie = getCookieForWallet(walletAddress);
            if (requiredCookie == null) {
                LOG.info("Cookie for wallet is not set. Need to log in (Hello) first.");
                return false;
            }
            String cookieFromReq = HttpUtil.getCookie(req, requiredCookie);
            if (cookieFromReq == null) {
                LOG.info("Cookie from request is null.");
                return false;
            }
            if (cookieFromReq.equals(requiredCookie)) {
                LOG.info("Request is authenticated: ");
                return true;
            }
            LOG.info("Cookies doesn't match. Required: " + requiredCookie + ", Received: " + cookieFromReq);
            return false;
        }
    }

}
