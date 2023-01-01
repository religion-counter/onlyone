package data;

public class Queries {

    // TODO Add column for casino winnings

    public static final String ALL_ACCOUNTS =
            "SELECT * FROM Accounts";

    public static final String WALLET_ADDRESS_COL = "wallet_address";
    public static final String DEPOSIT_ADDRESS_COL = "deposit_wallet_address";
    public static final String DEPOSIT_ADDRESS_PK_COL = "deposit_wallet_pk";
    public static final String BNB_BALANCE_COL = "bnb_balance";
    // If deposit address balance is more than deposit_bnb_balance - update (bnb_balance += difference)
    public static final String DEPOSIT_BNB_BALANCE_COL = "deposit_bnb_balance";
    public static final String ONLYONE_BALANCE_COL = "onlyone_balance";
    public static final String DEPOSIT_ONLYONE_BALANCE_COL = "deposit_onlyone_balance";
    public static final String AMOUNT_BNB_PLAYED_IN_CASINO = "amount_bnb_played_in_casino";
    public static final String AMOUNT_BNB_WON_IN_CASINO = "amount_bnb_won_in_casino";
    public static final String AMOUNT_ONLYONE_PLAYED_IN_CASINO = "amount_onlyone_played_in_casino";
    public static final String AMOUNT_ONLYONE_WON_IN_CASINO = "amount_onlyone_won_in_casino";

    public static final String ADD_ACCOUNT =
            "INSERT INTO Accounts (" +
                    WALLET_ADDRESS_COL + ", " +
                    DEPOSIT_ADDRESS_COL + ", " +
                    DEPOSIT_ADDRESS_PK_COL + ", " +
                    BNB_BALANCE_COL + ", " +
                    DEPOSIT_BNB_BALANCE_COL + ", " +
                    ONLYONE_BALANCE_COL + ", " +
                    DEPOSIT_ONLYONE_BALANCE_COL + ", " +
                    AMOUNT_BNB_PLAYED_IN_CASINO + ", " +
                    AMOUNT_BNB_WON_IN_CASINO + ", " +
                    AMOUNT_ONLYONE_PLAYED_IN_CASINO + ", " +
                    AMOUNT_ONLYONE_WON_IN_CASINO + ") " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public static final String UPDATE_ACCOUNT =
            "UPDATE Accounts " +
            "SET " + DEPOSIT_ADDRESS_COL + " = ? , " +
                    DEPOSIT_ADDRESS_PK_COL + " = ? , " +
                    BNB_BALANCE_COL + " = ? , " +
                    DEPOSIT_BNB_BALANCE_COL + " = ? , " +
                    ONLYONE_BALANCE_COL + " = ? , " +
                    DEPOSIT_ONLYONE_BALANCE_COL + " = ? , " +
                    AMOUNT_BNB_PLAYED_IN_CASINO + " = ? , " +
                    AMOUNT_BNB_WON_IN_CASINO + " = ? , " +
                    AMOUNT_ONLYONE_PLAYED_IN_CASINO + " = ? , " +
                    AMOUNT_ONLYONE_WON_IN_CASINO + " = ? " +
            "WHERE " + WALLET_ADDRESS_COL + " = ? ;";

    public static final String ACCOUNT_BY_WALLET = "" +
            "SELECT " +
                WALLET_ADDRESS_COL + ", " +
                DEPOSIT_ADDRESS_COL + ", " +
                DEPOSIT_ADDRESS_PK_COL + ", " +
                BNB_BALANCE_COL + ", " +
                DEPOSIT_BNB_BALANCE_COL + ", " +
                ONLYONE_BALANCE_COL + ", " +
                DEPOSIT_ONLYONE_BALANCE_COL + ", " +
                AMOUNT_BNB_PLAYED_IN_CASINO + ", " +
                AMOUNT_BNB_WON_IN_CASINO + ", " +
                AMOUNT_ONLYONE_PLAYED_IN_CASINO + ", " +
                AMOUNT_ONLYONE_WON_IN_CASINO + " " +
            "FROM Accounts " +
            "WHERE " + WALLET_ADDRESS_COL +
            " = ?";

    public final static String TABLE_DESC =
            "CREATE TABLE IF NOT EXISTS Accounts (" +
                    WALLET_ADDRESS_COL + " string primary key, " +
                    DEPOSIT_ADDRESS_COL + " string, " +
                    DEPOSIT_ADDRESS_PK_COL + " string, " +
                    BNB_BALANCE_COL + " string, " +
                    DEPOSIT_BNB_BALANCE_COL + " string, " +
                    ONLYONE_BALANCE_COL + " string, " +
                    DEPOSIT_ONLYONE_BALANCE_COL + " string, " +
                    AMOUNT_BNB_PLAYED_IN_CASINO + " string, " +
                    AMOUNT_BNB_WON_IN_CASINO + " string, " +
                    AMOUNT_ONLYONE_PLAYED_IN_CASINO + " string, " +
                    AMOUNT_ONLYONE_WON_IN_CASINO + " string " +
                    ")";
}
