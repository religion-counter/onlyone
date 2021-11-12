package mypackage;

import global.PriceService;

import java.math.BigDecimal;

public class PriceTest {
    public static void main(String[] args) throws Exception {

        PriceService ps = PriceService.INSTANCE;

        BigDecimal priceUsd = ps.getOnlyonePriceBusd();

        BigDecimal priceBnb = ps.getOnlyonePriceBnb();

        System.out.println("Price of Onlyone in $ is: " + priceUsd + "$");
        System.out.println("Price of Onlyone in BNB is: " + priceBnb + " BNB");
    }
}
