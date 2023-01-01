package global;

import BUSD.BEP20Token;
import mypackage.Web3Service;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.logging.Logger;

public class PriceService {
    private final static Logger LOG = Logger.getLogger(PriceService.class.getName());

    public static final String ONLYONE_BNB_PAIR_ADDRESS = "0xd22fa770dad9520924217b51bf7433c4a26067c2";
    public static final String ONLYONE_BUSD_PAIR_ADDRESS = "0x6a14173CFd03fac5ac9467B185F3F561Eb862568";

    public final static PriceService INSTANCE = new PriceService();

    private Web3Service _web3 = Web3Service.INSTANCE;

    private PriceService() {}

    public BigDecimal getOnlyonePriceBnb() throws Exception {
        BigInteger bnbs = _web3.getWbnb().balanceOf(ONLYONE_BNB_PAIR_ADDRESS).send();
        BigInteger onlyones = _web3.getOnlyone().balanceOf(ONLYONE_BNB_PAIR_ADDRESS).send();


        return new BigDecimal(bnbs).divide(new BigDecimal(onlyones), 18, RoundingMode.HALF_UP);
    }

    public BigDecimal getOnlyonePriceBusd() throws Exception {
        BigInteger busds = _web3.getBusd().balanceOf(ONLYONE_BUSD_PAIR_ADDRESS).send();
        BigInteger onlyones = _web3.getOnlyone().balanceOf(ONLYONE_BUSD_PAIR_ADDRESS).send();


        return new BigDecimal(busds).divide(new BigDecimal(onlyones), 18, RoundingMode.HALF_UP);
    }
}
