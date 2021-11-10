package mypackage;

import com.onlyonefinance.ONLYONE;
import global.Constants;
import global.Locks;
import global.OnlyoneGasProvider;
import global.WalletUtil;
import org.web3j.contracts.token.ERC20Interface;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Web3Service {

    private static final Logger LOG = Logger.getLogger(Web3Service.class.getName());

    public static final Web3Service INSTANCE = new Web3Service();

    private final Locks _locks = Locks.INSTANCE;

    private final Web3j _web3j;

    private final ONLYONE _onlyone;

    private Web3Service() {
        LOG.info("Initializing Web3J service.");
        try {

            Web3jService service = new HttpService("https://bsc-dataseed.binance.org/");
            _web3j = Web3j.build(service);

            LOG.info("Initialized web3j service: " + _web3j);

            Credentials master = WalletUtil.getMasterWallet();
            _onlyone = ONLYONE.load(Constants.ONLYONE_CONTRACT_ADDRESS, _web3j,
                    master, OnlyoneGasProvider.INSTANCE);
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, "Couldn't initialize Web3jService", t);
            throw t;
        }
    }

    public ONLYONE getOnlyone() {
        return _onlyone;
    }

    public Web3j getWeb3j() {
        return _web3j;
    }

    public String getBalanceWei(String wallet) throws Exception {
        synchronized (_locks.getLockForWallet(wallet)) {
            // send asynchronous requests to get balance
            EthGetBalance ethGetBalance = _web3j
                    .ethGetBalance(wallet, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            BigInteger wei = ethGetBalance.getBalance();
            return wei.toString();
        }
    }

    public String getTokenBalance() {
        throw new RuntimeException("Not implemented yet");
    }

    public String sendFunds(Credentials fromWallet, String toWallet, BigDecimal amount) {
        String fromAddress = null;
        try {
            fromAddress = fromWallet.getAddress();
            if (fromAddress == null) {
                throw new Exception("From address is null.");
            }
            synchronized (_locks.getLockForWallet(fromAddress)) {
                BigDecimal amountToSend = amount;
                amountToSend = amountToSend.round(MathContext.DECIMAL64);
                TransactionReceipt receipt = Transfer.sendFunds(_web3j,
                        fromWallet,
                        toWallet,
                        amountToSend,
                        Convert.Unit.ETHER)
                        .sendAsync()
                        .get();
                return receipt.getTransactionHash();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Couldn't send " + amount + " ether to " + toWallet + " from " + fromAddress, e);
            return null;
        }
    }
}
