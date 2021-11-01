package data;

public class DataService {
    public static final DataService INSTANCE = new DataService();
    private final DatabaseService _databaseService = DatabaseService.INSTANCE;
    private DataService() {}

    public Account getAccount(String walletAddress) {
        return _databaseService.getAccount(walletAddress);
    }

    public void addAccount(Account account) {
        _databaseService.addAccount(account);
    }

    public synchronized boolean updateAccount(Account account) {
        return _databaseService.updateAccount(account);
    }
}
