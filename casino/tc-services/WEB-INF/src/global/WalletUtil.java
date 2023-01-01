package global;

import org.web3j.crypto.Credentials;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletUtil {

    private static final Logger LOG = Logger.getLogger(WalletUtil.class.getName());

    public static Credentials getMasterWallet() {
        try {
            File f = new File("/etc/onlyone/acc");
            BufferedReader is = new BufferedReader(new FileReader(f));
            String masterAddr = is.readLine();
            String masterPk = is.readLine();
            Credentials result = Credentials.create(masterPk);
            LOG.info("Extracted master wallet: " + masterAddr);
            is.close();
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Couldn't get master wallet: ", e);
            return null;
        }
    }
}
