package global;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Locks {

    private static final Logger LOG = Logger.getLogger(Locks.class.getName());

    public static final Locks INSTANCE = new Locks();

    private Locks() {}

    private final ConcurrentHashMap<String, Object> _locksPerWallet = new ConcurrentHashMap<>();

    public synchronized Object getLockForWallet(String wallet) {
        Object lock = _locksPerWallet.get(wallet);
        if (lock == null) {
            LOG.info("Creating lock for: " + wallet);
            lock = new Object();
            _locksPerWallet.put(wallet, lock);
        }
        return lock;
    }

}
