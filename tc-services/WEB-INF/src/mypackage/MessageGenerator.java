package mypackage;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

public class MessageGenerator {

    public SecureRandom random = new SecureRandom();

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
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                sb.append((char)('a' + (char) random.nextInt(20)));
            }
            this.message = sb.toString();
            timeWhenMessageIsGenerated = System.currentTimeMillis();
        }
    }

}
