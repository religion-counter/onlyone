package mypackage;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CookieService {

    private static final Logger LOG = Logger.getLogger(CookieService.class.getName());

    public static final CookieService INSTANCE = new CookieService();

    private SecureRandom random = new SecureRandom();

    private CookieService() {}

    private final ConcurrentHashMap<String, String> cookieToWallet = new ConcurrentHashMap<>();

    public synchronized String getCookieForWallet(String wallet) {
        if (cookieToWallet.containsKey(wallet)) {
            return cookieToWallet.get(wallet);
        }
        return null;
    }

    public synchronized String generateNewCookieForWallet(String wallet) {
        StringBuilder cookie = new StringBuilder();
        for (int i = 0; i < 26; ++i) {
            cookie.append((char)('a' + random.nextInt(25)));
        }
        String cookieStr = cookie.toString();
        cookieToWallet.put(wallet, cookieStr);
        LOG.info("Generated new cookie: " +
                HttpUtil.ONLYONE_COOKIE + ":" + cookieStr + " for " + wallet);
        return cookieStr;
    }

    public synchronized boolean isRequestAuthenticated(HttpServletRequest req) {
        if (req == null) return false;
        String walletAddress = req.getHeader(HttpUtil.WALLET_HEADER);
        if (walletAddress == null) {
            LOG.info("Request is without a wallet in the header.");
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
