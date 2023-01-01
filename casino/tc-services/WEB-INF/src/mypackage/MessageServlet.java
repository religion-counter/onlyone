package mypackage;

import global.Locks;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class MessageServlet  extends HttpServlet {

    private final Logger LOG = Logger.getLogger(MessageServlet.class.getName());

    private final Locks _locks = Locks.INSTANCE;
    private final MessageGenerator _messageGenerator = MessageGenerator.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get wallet from req header
        // Pug response in resp
        String wallet = req.getHeader(HttpUtil.WALLET_HEADER);
        if (wallet == null) {
            LOG.severe("Requesting sign message for null wallet.");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        synchronized (_locks.getLockForWallet(wallet)) {
            MessageGenerator.Message msg = _messageGenerator.getMessage(wallet, true);
            HttpUtil.postResponse(resp, msg.message);
        }
    }
}
