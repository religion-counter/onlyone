package global;

import java.math.BigDecimal;

public class Constants {
    public static final BigDecimal TRANSFER_FEE = new BigDecimal("0.000105");
    public static final BigDecimal WITHDRAW_TAX = TRANSFER_FEE.multiply(new BigDecimal("2"));
    public static final String ONLYONE_CONTRACT_ADDRESS = "0xb899dB682e6D6164D885ff67C1e676141deaaA40";
}
