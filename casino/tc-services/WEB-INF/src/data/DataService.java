package data;

public class DataService {
    public static final DataService INSTANCE = new DataService();
    private final DatabaseService _databaseService = DatabaseService.INSTANCE;
    private DataService() {}

    public Account getAccount(String walletAddress) {
        return _databaseService.getAccount(walletAddress);
    }

    public boolean addAccount(Account account) {
        return _databaseService.addAccount(account);
    }

    public boolean updateAccount(Account account) {
        return _databaseService.updateAccount(account);
    }
}
