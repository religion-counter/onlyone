package data;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseService {

    public static final Logger LOG = Logger.getLogger(DatabaseService.class.getName());

    public static final DatabaseService INSTANCE = new DatabaseService();
    private Connection _connection;

    private DatabaseService() {
        LOG.info("Initializing DataBase");
        for (int i = 0; i < 3; ++i) {
            if (get_connection()) {
                return;
            }
        }
    }

    public Account getAccount(String walletAddress) {
        try {
            PreparedStatement statement = _connection.prepareStatement(Queries.ACCOUNT_BY_WALLET);
            statement.setString(1, walletAddress);

            LOG.info("Executing " + Queries.ACCOUNT_BY_WALLET + " to get account.");
            ResultSet account = statement.executeQuery();

            String walletAddressForCheck = account.getString(Queries.WALLET_ADDRESS_COL);
            if (!walletAddress.equals(walletAddressForCheck)) {
                return null;
            }
            String depositAddress = account.getString(Queries.DEPOSIT_ADDRESS_COL);
            String depositPk = account.getString(Queries.DEPOSIT_ADDRESS_PK_COL);
            String bnbBalance = account.getString(Queries.BNB_BALANCE_COL);
            String depositBnbBalance = account.getString(Queries.DEPOSIT_BNB_BALANCE_COL);
            return new Account(walletAddress, depositAddress, depositPk, Double.parseDouble(bnbBalance),
                    Double.parseDouble(depositBnbBalance));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error executing query: ", e);
        }
        return null;
    }

    public synchronized boolean updateAccount(Account account) {
        try {
            PreparedStatement statement = _connection.prepareStatement(Queries.UPDATE_ACCOUNT);
            statement.setString(1, account.depositWalletAddress);
            statement.setString(2, account.depositWalletPk);
            statement.setString(3, String.valueOf(account.bnbBalance));
            statement.setString(4, String.valueOf(account.depositBnbBalance));
            statement.setString(5, account.walletAddress);
            LOG.info("Executing " + Queries.UPDATE_ACCOUNT + " to update an account in the Database.");
            int update = statement.executeUpdate();
            LOG.info("Accounts updated: " + update);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error updating account in the Database: ", e);
        }
        return false;
    }

    public void addAccount(Account account) {
        try {
            PreparedStatement statement = _connection.prepareStatement(Queries.ADD_ACCOUNT);
            statement.setString(1, account.walletAddress);
            statement.setString(2, account.depositWalletAddress);
            statement.setString(3, account.depositWalletPk);
            statement.setString(4, String.valueOf(account.bnbBalance));
            statement.setString(5, String.valueOf(account.depositBnbBalance));
            LOG.info("Executing " + Queries.ADD_ACCOUNT + " to add an account to the Database.");
            int update = statement.executeUpdate();
            LOG.info("Accounts added: " + update);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error adding account to the Database: ", e);
        }
    }


    private boolean get_connection() {
        try {
            // create a database connection
            new SQLiteDataSource(); // needed for the side effect
            _connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = _connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            LOG.info("Creating table if doesn't exist with: " + Queries.TABLE_DESC);
            statement.executeUpdate(Queries.TABLE_DESC);
            LOG.info(getAllAccounts());
            return true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeSqlConnection();
    }

    private void closeSqlConnection() {
        try {
            if (_connection != null) {
                _connection.close();
            }
        } catch(SQLException e) {
            LOG.log(Level.SEVERE, "Error closing SQL connection: ", e);
            // connection close failed.
        }
    }

    public String getAllAccounts() {
        try {
            StringBuilder res = new StringBuilder("Printing all accounts on startup:");
            ResultSet rs = _connection.prepareStatement(Queries.ALL_ACCOUNTS).executeQuery();
            while (rs.next()) {
                res.append("Address: ").append(rs.getString(Queries.WALLET_ADDRESS_COL)).append('\n')
                        .append("Deposit address: ").append(rs.getString(Queries.DEPOSIT_ADDRESS_COL)).append('\n')
                        .append("Deposit PK: ").append(rs.getString(Queries.DEPOSIT_ADDRESS_PK_COL)).append('\n')
                        .append("BNB balance: ").append(rs.getString(Queries.BNB_BALANCE_COL)).append('\n')
                        .append("Deposit BNB Balance: ").append(rs.getString(Queries.DEPOSIT_BNB_BALANCE_COL)).append('\n')
                        .append('\n');
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}