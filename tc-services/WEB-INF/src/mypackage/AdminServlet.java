package mypackage;

import data.Account;
import data.DatabaseService;
import global.WalletUtil;
import org.web3j.crypto.Credentials;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminServlet extends HttpServlet {

    private final static Logger LOG = Logger.getLogger(AdminServlet.class.getName());

    private final static String OPERATION_HEADER = "OPERATION";
    private final static String GET_ALL_ACCOUNTS = "GET_ALL_ACCOUNTS";
    private final static String ADD_ACCOUNTS = "ADD_ACCOUNTS";
    private final static String ACCOUNTS_TO_ADD = "ACCOUNTS_TO_ADD";

    private final MessageGenerator _messageGenerator = MessageGenerator.INSTANCE;
    private final DatabaseService _databaseService = DatabaseService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Check for admin access ( master wallet )

        String walletAddress = req.getHeader(HttpUtil.WALLET_HEADER);
        if (walletAddress == null) {
            LOG.info("Received doGet request without wallet. Ignoring.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        LOG.info("Received doGet request for wallet: " + walletAddress);
        String signature = req.getHeader(HttpUtil.SIGNATURE_HEADER);
        if (_messageGenerator.isSignatureInvalid(walletAddress, signature)) {
            LOG.info("Signature of request is not valid. Ignoring");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        Credentials masterWallet = WalletUtil.getMasterWallet();

        if (masterWallet == null || !walletAddress.equalsIgnoreCase(masterWallet.getAddress())) {
            LOG.info("Admin page is only for master wallet...");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        try {
            String operation = req.getHeader(OPERATION_HEADER);
            if (GET_ALL_ACCOUNTS.equals(operation)) {
                String allAccounts = getAllAccounts();
                HttpUtil.postResponse(resp, allAccounts);
                return;
            } else if (ADD_ACCOUNTS.equals(operation)) {
                String accountsToAdd = req.getHeader(ACCOUNTS_TO_ADD);
                String addResult = addAccounts(accountsToAdd);
                HttpUtil.postResponse(resp, addResult);
                return;
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.log(Level.SEVERE, "Error executing admin operation: ", e);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public String addAccounts(String accountsToAdd) {
        throw new RuntimeException("Not implemented yet: " + accountsToAdd + " use the cli");
    }


    public String getAllAccounts() {
        List<Account> accounts = _databaseService.getAllAccounts();
        return Arrays.deepToString(accounts.toArray(new Account[0]));
    }
}
