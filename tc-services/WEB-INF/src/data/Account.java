package data;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {

    private static final Logger LOG = Logger.getLogger(Account.class.getName());

    public String walletAddress;
    public String depositWalletAddress;
    public String depositWalletPk;
    public BigDecimal bnbBalance;
    public BigDecimal depositBnbBalance;
    public BigDecimal onlyoneBalance;
    public BigDecimal depositOnlyoneBalance;
    public BigDecimal amountBnbPlayedInCasino;
    public BigDecimal amountBnbWonInCasino;
    public BigDecimal amountOnlyonePlayedInCasino;
    public BigDecimal amountOnlyoneWonInCasino;

    public Account(
            String walletAddress,
            String depositWalletAddress,
            String depositWalletPk,
            BigDecimal bnbBalance,
            BigDecimal depositBnbBalance,
            BigDecimal onlyoneBalance,
            BigDecimal depositOnlyoneBalance,
            BigDecimal amountBnbPlayedInCasino,
            BigDecimal amountBnbWonInCasino,
            BigDecimal amountOnlyonePlayedInCasino,
            BigDecimal amountOnlyoneWonInCasino
    ) {
        this.walletAddress = walletAddress;
        this.depositWalletAddress = depositWalletAddress;
        this.depositWalletPk = depositWalletPk;
        this.bnbBalance = bnbBalance;
        this.depositBnbBalance = depositBnbBalance;
        this.onlyoneBalance = onlyoneBalance;
        this.depositOnlyoneBalance = depositOnlyoneBalance;
        this.amountBnbPlayedInCasino = amountBnbPlayedInCasino;
        this.amountBnbWonInCasino = amountBnbWonInCasino;
        this.amountOnlyonePlayedInCasino = amountOnlyonePlayedInCasino;
        this.amountOnlyoneWonInCasino = amountOnlyoneWonInCasino;
    }

    public static Account generateNew(String walletAddress) {
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();

            String depositPrivateKeyInHex = privateKeyInDec.toString(16);

            WalletFile depositWallet = Wallet.createLight("", ecKeyPair);
            String depositAddress = "0x" + depositWallet.getAddress();
            LOG.info("Generated a new account for : " + walletAddress +
                    " with deposit address: " + depositAddress);
            return new Account(
                    walletAddress,
                    depositAddress,
                    depositPrivateKeyInHex,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error generating new account: ", e);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Account{" +
                "walletAddress='" + walletAddress + '\'' +
                ", depositWalletAddress='" + depositWalletAddress + '\'' +
                ", depositWalletPk='" + depositWalletPk + '\'' +
                ", bnbBalance=" + bnbBalance +
                ", depositBnbBalance=" + depositBnbBalance +
                ", onlyoneBalance=" + onlyoneBalance +
                ", depositOnlyoneBalance=" + depositOnlyoneBalance +
                ", amountBnbPlayedInCasino=" + amountBnbPlayedInCasino +
                ", amountBnbWonInCasino=" + amountBnbWonInCasino +
                ", amountOnlyonePlayedInCasino=" + amountOnlyonePlayedInCasino +
                ", amountOnlyoneWonInCasino=" + amountOnlyoneWonInCasino +
                '}';
    }
}
