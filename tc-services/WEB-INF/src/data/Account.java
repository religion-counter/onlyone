package data;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account {

    private static final Logger LOG = Logger.getLogger(Account.class.getName());

    public String walletAddress;
    public String depositWalletAddress;
    public String depositWalletPk;
    public double bnbBalance;
    public double depositBnbBalance;

    public Account(
            String walletAddress,
            String depositWalletAddress,
            String depositWalletPk,
            double bnbBalance,
            double depositBnbBalance
    ) {
        this.walletAddress = walletAddress;
        this.depositWalletAddress = depositWalletAddress;
        this.depositWalletPk = depositWalletPk;
        this.bnbBalance = bnbBalance;
        this.depositBnbBalance = depositBnbBalance;
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
                    0,
                    0);
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
                ", depositWalletPk='CENSORED'" +
                ", bnbBalance=" + bnbBalance +
                ", depositBnbBalance=" + depositBnbBalance +
                '}';
    }
}
