package global;

public class GlobalApplicationLock {

    public static final GlobalApplicationLock INSTANCE = new GlobalApplicationLock();

    private GlobalApplicationLock() {}
}
