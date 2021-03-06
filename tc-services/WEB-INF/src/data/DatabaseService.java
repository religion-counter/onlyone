package data;

import global.Locks;
import org.sqlite.SQLiteDataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseService {

    public static final Logger LOG = Logger.getLogger(DatabaseService.class.getName());

    public static final DatabaseService INSTANCE = new DatabaseService();
    private final Locks _locks = Locks.INSTANCE;
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
        synchronized (_locks.getLockForWallet(walletAddress)) {
            try {
                PreparedStatement statement = _connection.prepareStatement(Queries.ACCOUNT_BY_WALLET);
                statement.setString(1, walletAddress);

                LOG.info("Executing " + Queries.ACCOUNT_BY_WALLET + " to get account.");
                ResultSet account = statement.executeQuery();

                String walletAddressForCheck = null;
                if (account.next()) {
                    walletAddressForCheck = account.getString(Queries.WALLET_ADDRESS_COL);
                }
                if (walletAddressForCheck == null) {
                    LOG.info("Account doesn't exist in DB: " + walletAddress);
                    return null;
                }

                if (!walletAddress.equals(walletAddressForCheck)) {
                    LOG.log(Level.SEVERE, "Account for check doesn't match requested account. "
                            + "RequestedAccount: " + walletAddress +
                            ", Account in DB:"  + walletAddressForCheck);
                    return null;
                }
                String depositAddress = account.getString(Queries.DEPOSIT_ADDRESS_COL);
                String depositPk = account.getString(Queries.DEPOSIT_ADDRESS_PK_COL);
                String bnbBalance = account.getString(Queries.BNB_BALANCE_COL);
                String depositBnbBalance = account.getString(Queries.DEPOSIT_BNB_BALANCE_COL);
                String onlyoneBalance = account.getString(Queries.ONLYONE_BALANCE_COL);
                String depositOnlyoneBalance = account.getString(Queries.DEPOSIT_ONLYONE_BALANCE_COL);
                String amountBnbPlayedInCasino = account.getString(Queries.AMOUNT_BNB_PLAYED_IN_CASINO);
                String amountBnbWonInCasino = account.getString(Queries.AMOUNT_BNB_WON_IN_CASINO);
                String amountOnlyonePlayedInCasino = account.getString(Queries.AMOUNT_ONLYONE_PLAYED_IN_CASINO);
                String amountOnlyoneWonInCasino = account.getString(Queries.AMOUNT_ONLYONE_WON_IN_CASINO);
                return new Account(walletAddress, depositAddress, depositPk,
                        new BigDecimal(bnbBalance),
                        new BigDecimal(depositBnbBalance),
                        new BigDecimal(onlyoneBalance),
                        new BigDecimal(depositOnlyoneBalance),
                        new BigDecimal(amountBnbPlayedInCasino),
                        new BigDecimal(amountBnbWonInCasino),
                        new BigDecimal(amountOnlyonePlayedInCasino),
                        new BigDecimal(amountOnlyoneWonInCasino)
                );
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error executing query: ", e);
            }
            return null;
        }
    }

    public boolean updateAccount(Account account) {
        synchronized (_locks.getLockForWallet(account.walletAddress)) {
            try {
                PreparedStatement statement = _connection.prepareStatement(Queries.UPDATE_ACCOUNT);
                statement.setString(1, account.depositWalletAddress);
                statement.setString(2, account.depositWalletPk);
                statement.setString(3, account.bnbBalance.toString());
                statement.setString(4, account.depositBnbBalance.toString());
                statement.setString(5, account.onlyoneBalance.toString());
                statement.setString(6, account.depositOnlyoneBalance.toString());
                statement.setString(7, account.amountBnbPlayedInCasino.toString());
                statement.setString(8, account.amountBnbWonInCasino.toString());
                statement.setString(9, account.amountOnlyonePlayedInCasino.toString());
                statement.setString(10, account.amountOnlyoneWonInCasino.toString());
                statement.setString(11, account.walletAddress);
                LOG.info("Executing " + Queries.UPDATE_ACCOUNT + " to update an account in the Database.");
                int update = statement.executeUpdate();
                LOG.info("Accounts updated: " + update);
                return true;
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error updating account in the Database: ", e);
            }
            return false;
        }
    }

    public boolean addAccount(Account account) {
        synchronized (_locks.getLockForWallet(account.walletAddress)) {
            try {
                PreparedStatement statement = _connection.prepareStatement(Queries.ADD_ACCOUNT);
                statement.setString(1, account.walletAddress);
                statement.setString(2, account.depositWalletAddress);
                statement.setString(3, account.depositWalletPk);
                statement.setString(4, account.bnbBalance.toString());
                statement.setString(5, account.depositBnbBalance.toString());
                statement.setString(6, account.onlyoneBalance.toString());
                statement.setString(7, account.depositOnlyoneBalance.toString());
                statement.setString(8, account.amountBnbPlayedInCasino.toString());
                statement.setString(9, account.amountBnbWonInCasino.toString());
                statement.setString(10, account.amountOnlyonePlayedInCasino.toString());
                statement.setString(11, account.amountOnlyoneWonInCasino.toString());
                LOG.info("Executing " + Queries.ADD_ACCOUNT + " to add an account to the Database.");
                int update = statement.executeUpdate();
                LOG.info("Accounts added: " + update);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error adding account to the Database: ", e);
                return false;
            }
        }
        return true;
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
            List<Account> accounts = getAllAccounts();
            System.out.println("There are " + accounts.size() + " accounts.");
            return true;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void closeSqlConnection() {
        try {
            if (_connection != null) {
                _connection.close();
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error closing SQL connection: ", e);
            // connection close failed.
        }
    }

    public List<Account> getAllAccounts() {
        ArrayList<Account> result = new ArrayList<>();
        try {
            StringBuilder logMessage = new StringBuilder("Printing all accounts on startup:");
            ResultSet rs = _connection.prepareStatement(Queries.ALL_ACCOUNTS).executeQuery();
            while (rs.next()) {
                String walletAddress = rs.getString(Queries.WALLET_ADDRESS_COL);
                String depositWalletAddress = rs.getString(Queries.DEPOSIT_ADDRESS_COL);
                String depositPk = rs.getString(Queries.DEPOSIT_ADDRESS_PK_COL);
                String bnbBalance = rs.getString(Queries.BNB_BALANCE_COL);
                String depositWalletBnbBalance = rs.getString(Queries.DEPOSIT_BNB_BALANCE_COL);
                String onlyoneBalance = rs.getString(Queries.ONLYONE_BALANCE_COL);
                String depositOnlyoneBalance = rs.getString(Queries.DEPOSIT_BNB_BALANCE_COL);
                String amountBnbPlayedInCasino = rs.getString(Queries.AMOUNT_BNB_PLAYED_IN_CASINO);
                String amountBnbWonInCasino = rs.getString(Queries.AMOUNT_BNB_WON_IN_CASINO);
                String amountOnlyonePlayedInCasino = rs.getString(Queries.AMOUNT_ONLYONE_PLAYED_IN_CASINO);
                String amountOnlyoneWonInCasino = rs.getString(Queries.AMOUNT_ONLYONE_WON_IN_CASINO);
                Account account = new Account(
                        walletAddress, depositWalletAddress, depositPk,
                        new BigDecimal(bnbBalance),
                        new BigDecimal(depositWalletBnbBalance),
                        new BigDecimal(onlyoneBalance),
                        new BigDecimal(depositOnlyoneBalance),
                        new BigDecimal(amountBnbPlayedInCasino),
                        new BigDecimal(amountBnbWonInCasino),
                        new BigDecimal(amountOnlyonePlayedInCasino),
                        new BigDecimal(amountOnlyoneWonInCasino)
                );
                result.add(account);
                logMessage.append(account);
            }
            LOG.info(logMessage.toString());
            return result;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error getting all accounts information: ", e);
        }
        return null;
    }
}
