package mypackage;

import data.Account;
import data.DataService;
import global.Locks;
import org.jetbrains.annotations.VisibleForTesting;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FortyServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(FortyServlet.class.getName());

    private static final String ONE_TO_TWENTY = "ONE_TO_TWENTY";
    private static final String TWENTY_ONE_TO_FORTY = "TWENTY_ONE_TO_FORTY";
    private static final String ODDS = "ODDS";
    private static final String EVENS = "EVENS";
    private static final String NUMBER_PREFIX = "NUMBER_";

    private static final BigDecimal MAX_BET_AMOUNT = new BigDecimal("0.0005");
    private static final BigDecimal MIN_BET_AMOUNT = new BigDecimal("0.00001");

    private final CookieService _cookieService;
    private final DataService _dataService;
    private final Locks _locks;
    private final SecureRandom _secureRandom = new SecureRandom();

    public FortyServlet() {
        this._cookieService = CookieService.INSTANCE;
        this._dataService = DataService.INSTANCE;
        this._locks = Locks.INSTANCE;
    }

//    @VisibleForTesting
//    FortyServlet(CookieService cookieService, DataService dataService, Locks locks) {
//        this._cookieService = cookieService;
//        this._dataService = dataService;
//        this._locks = locks;
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.info("Incoming forty game request.");
        String wallet = req.getHeader(HttpUtil.WALLET_HEADER);
        if (wallet == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.log(Level.SEVERE, "Empty wallet header");
            return;
        }
        synchronized (_locks.getLockForWallet(wallet)) {
            if (_cookieService.isRequestUnauthenticated(req)) {
                LOG.info("Request is not authenticated");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Account acc = _dataService.getAccount(wallet);
            if (acc == null) {
                LOG.severe("Couldn't get account for wallet " + wallet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            String betAmount = req.getHeader(HttpUtil.BET_AMOUNT_HEADER);
            if (betAmount == null) {
                LOG.severe("Null BET_AMOUNT header for wallet " + acc.walletAddress);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            BigDecimal betAmountDecimal;
            try {
                betAmountDecimal = new BigDecimal(betAmount);
            } catch (Exception e) {
                LOG.severe("Invalid BET_AMOUNT header: " + betAmount + " for wallet " + wallet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (betAmountDecimal.compareTo(acc.bnbBalance) > 0) {
                LOG.severe("Trying to bet more than BNB balance. Amount to bet: " +
                        betAmountDecimal + ", BNB balance: " + acc.bnbBalance + " for wallet " + wallet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (betAmountDecimal.compareTo(MAX_BET_AMOUNT) > 0) {
                LOG.severe("Trying to bet more than the allowed maximum. Amount to bet: " +
                        betAmountDecimal + " for wallet " + wallet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (betAmountDecimal.compareTo(MIN_BET_AMOUNT) < 0) {
                LOG.severe("Trying to bet less than the allowed minimum. Amount to bet: " +
                        betAmountDecimal + " for wallet " + wallet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            String selectedBet = req.getHeader(HttpUtil.SELECTED_BET_HEADER);
            if (!isSelectedBetValid(selectedBet)) {
                LOG.log(Level.SEVERE, "Invalid selected bet: " + selectedBet);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            int chosenNumber = _secureRandom.nextInt(41);
            int winningMultiplier = playerWinningMultiplier(selectedBet, chosenNumber);
            BigDecimal amountWon = betAmountDecimal.multiply(new BigDecimal(winningMultiplier));
            acc.bnbBalance = acc.bnbBalance.subtract(betAmountDecimal);
            acc.bnbBalance = acc.bnbBalance.add(amountWon);

            if (!_dataService.updateAccount(acc)) {
                LOG.severe("Couldn't update account after withdraw. FATAL Crashing the server");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                System.exit(1);
                return;
            }

            // Sleep 1 second to delay the response.
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "Sleep was interrupted for wallet: " + wallet, e);
            }
            HttpUtil.postResponse(resp, chosenNumber + ":" + acc.bnbBalance + ":" + amountWon);
        }
    }

    private int playerWinningMultiplier(String selectedBet, int chosenNumber) {
        if (selectedBet == null) {
            return 0;
        }
        switch (selectedBet) {
            case ONE_TO_TWENTY:
                if ((chosenNumber >= 1) && (chosenNumber <= 20)) {
                    return 2;
                } else {
                    return 0;
                }
            case TWENTY_ONE_TO_FORTY:
                if ((chosenNumber >= 21) && (chosenNumber <= 40)) {
                    return 2;
                } else {
                    return 0;
                }
            case ODDS:
                if (chosenNumber % 2 == 1) {
                    return 2;
                } else {
                    return 0;
                }
            case EVENS:
                if ((chosenNumber != 0) && (chosenNumber % 2 == 0)) {
                    return 2;
                } else {
                    return 0;
                }
            default:
                if (selectedBet.startsWith(NUMBER_PREFIX)) {
                    String[] parts = selectedBet.split("_");
                    if (parts.length != 2) {
                        return 0;
                    }
                    try {
                        int i = Integer.valueOf(parts[1], 10);
                        if (i >= 0 && i <= 40) {
                            if (i == chosenNumber) {
                                return 40;
                            } else {
                                return 0;
                            }
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Cannot parse number: " + parts[1]);
                    }
                }
        }
        return 0;
    }

    private boolean isSelectedBetValid(String selectedBet) {
        if (selectedBet == null) {
            return false;
        }
        switch (selectedBet) {
            case ONE_TO_TWENTY:
            case TWENTY_ONE_TO_FORTY:
            case ODDS:
            case EVENS:
                return true;
            default:
                if (selectedBet.startsWith(NUMBER_PREFIX)) {
                    String[] parts = selectedBet.split("_");
                    if (parts.length != 2) {
                        return false;
                    }
                    try {
                        int i = Integer.valueOf(parts[1], 10);
                        if (i >= 0 && i <= 40) {
                            return true;
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Cannot parse number: " + parts[1]);
                    }
                }
        }
        return false;
    }
}
