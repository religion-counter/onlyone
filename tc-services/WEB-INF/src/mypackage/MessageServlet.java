package mypackage;

import global.GlobalApplicationLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class MessageServlet  extends HttpServlet {
    private final MessageGenerator _messageGenerator = MessageGenerator.INSTANCE;

    private final Logger LOG = Logger.getLogger(MessageServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        synchronized (GlobalApplicationLock.INSTANCE) {
            // Get wallet from req header
            // Pug response in resp
            String wallet = req.getHeader(HttpUtil.WALLET_HEADER);
            if (wallet == null) {
                LOG.severe("Requesting sign message for null wallet.");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            MessageGenerator.Message msg = _messageGenerator.getMessage(wallet, true);
            HttpUtil.postResponse(resp, msg.message);
        }
    }
}
