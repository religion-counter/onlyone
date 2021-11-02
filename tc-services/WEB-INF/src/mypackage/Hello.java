package mypackage;

import data.Account;
import data.DataService;
import data.DatabaseService;
import global.GlobalApplicationLock;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import periodic.PeriodicCollectManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class Hello extends HttpServlet {

    // TODO Make LOGIN button and LOGIN goes through the Hello workflow.

    private static final Logger LOG = Logger.getLogger(Hello.class.getName());

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    private final MessageGenerator _messageGenerator = MessageGenerator.INSTANCE;
    private final DataService _dataservice = DataService.INSTANCE;
    private final PeriodicCollectManager _periodicCollectManager = PeriodicCollectManager.INSTANCE;

    @Override
    public void init() throws ServletException {
        synchronized (GlobalApplicationLock.INSTANCE) {
            _periodicCollectManager.startPeriodicCollectTask();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        synchronized (GlobalApplicationLock.INSTANCE) {
            String walletAddress = req.getHeader(HttpUtil.WALLET_HEADER);
            if (walletAddress == null) {
                LOG.info("Received doGet request without wallet. Ignoring.");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            LOG.info("Received doGet request for wallet: " + walletAddress);
            String signature = req.getHeader(HttpUtil.SIGNATURE_HEADER);
            if (!isSignatureValid(walletAddress, signature)) {
                LOG.info("Signature of request is not valid. Ignoring");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            boolean generateNewCookie = true;
            String existingCookie = CookieService.INSTANCE.getCookieForWallet(walletAddress);
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
                    _dataservice.addAccount(account);
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
                cookieForSet = CookieService.INSTANCE.generateNewCookieForWallet(walletAddress);
            } else {
                cookieForSet = CookieService.INSTANCE.getCookieForWallet(walletAddress);
            }
            Cookie c = new Cookie(HttpUtil.ONLYONE_COOKIE, cookieForSet);
            c.setPath("/");
            // c.setHttpOnly(true);
            // c.setSecure(true); TODO SET THESE IN PRODUCTION
            resp.addCookie(c);
            LOG.info("Adding cookie " + c.getName() + ":" + c.getValue() + " to the response.");
            String oldToken = CookieService.INSTANCE.getTokenForWallet(walletAddress);
            if (oldToken != null) {
                LOG.info("Replacing token for wallet: " + walletAddress);
            }
            String token = CookieService.INSTANCE.generateNewTokenForWallet(walletAddress);

            HttpUtil.postResponse(resp, account.depositWalletAddress + ":" + account.bnbBalance + ":" + token);
        }
    }

    private boolean isSignatureValid(
            String walletAddress,
            String signature
    ) {
        if (walletAddress == null) {
            return false;
        }

        MessageGenerator.Message messageForSign = _messageGenerator.getMessage(walletAddress, false);

        if (System.currentTimeMillis() - messageForSign.timeWhenMessageIsGenerated >
                TimeUnit.MINUTES.toMillis(5)) {
            return false;
        }

        String prefix = PERSONAL_MESSAGE_PREFIX + messageForSign.message.length();
        byte[] msgHash = Hash.sha3((prefix + messageForSign.message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        Sign.SignatureData sd = new Sign.SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered;
        try {
            // Iterate for each possible key to recover
            for (int i = 0; i < 4; i++) {
                BigInteger publicKey = Sign.recoverFromSignature(
                        (byte) i,
                        new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                        msgHash);

                if (publicKey != null) {
                    addressRecovered = "0x" + Keys.getAddress(publicKey);

                    if (addressRecovered.equalsIgnoreCase(walletAddress)) {
                        return true;
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    @Override
    public void destroy() {
        LOG.info("Destroying hello servlet");
        _periodicCollectManager.stopPeriodicCollectTask();
        DatabaseService.INSTANCE.closeSqlConnection();
    }
}

