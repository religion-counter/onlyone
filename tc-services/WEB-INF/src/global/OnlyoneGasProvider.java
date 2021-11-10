package global;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public class OnlyoneGasProvider implements ContractGasProvider {

    public static final OnlyoneGasProvider INSTANCE = new OnlyoneGasProvider();

    private final static BigInteger MIN_GAS_PRICE = new BigInteger("5");
    private final static BigInteger MIN_GAS_LIMIT = new BigInteger("21000");

    @Override
    public BigInteger getGasPrice(String s) {
        return MIN_GAS_PRICE;
    }

    @Override
    public BigInteger getGasPrice() {
        return MIN_GAS_PRICE;
    }

    @Override
    public BigInteger getGasLimit(String s) {
        return MIN_GAS_LIMIT;
    }

    @Override
    public BigInteger getGasLimit() {
        return MIN_GAS_LIMIT;
    }
}
