package oo;

import java.math.BigDecimal;

public interface ExchangeOrder extends HasPriceLevel {

    Side getSide();
    long getQuantity();
    long getLeavesQty();
}
