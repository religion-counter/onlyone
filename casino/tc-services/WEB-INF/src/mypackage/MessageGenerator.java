package mypackage;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageGenerator {

    private static final Logger LOG = Logger.getLogger(MessageGenerator.class.getName());

    public SecureRandom random = new SecureRandom();

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static MessageGenerator INSTANCE = new MessageGenerator();

    private MessageGenerator() {}

    private final ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

    public Message getMessage(String wallet, boolean generate) {
        if (!generate) {
            if (messages.containsKey(wallet)) {
                return messages.get(wallet);
            }
        }
        final Message res = new Message();
        messages.put(wallet, res);
        return res;
    }

    public class Message {
        final String message;
        final double timeWhenMessageIsGenerated;

        Message() {
            StringBuilder sb = new StringBuilder(
                    "Sign the following message in order to login to the casino: ");
            for (int i = 0; i < 16; ++i) {
                sb.append((char)('a' + (char) random.nextInt(20)));
            }
            this.message = sb.toString();
            timeWhenMessageIsGenerated = System.currentTimeMillis();
        }
    }

    public boolean isSignatureInvalid(
            String walletAddress,
            String signature
    ) {
        if (walletAddress == null) {
            return true;
        }

        MessageGenerator.Message messageForSign = getMessage(walletAddress, false);

        if (messageForSign == null) {
            return true;
        }

        if (System.currentTimeMillis() - messageForSign.timeWhenMessageIsGenerated >
                TimeUnit.MINUTES.toMillis(5)) {
            return true;
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
                        return false;
                    }
                }
            }
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, "Exception while validating signature: ", t);
            t.printStackTrace();
        }
        return true;
    }

}
