package oo;

import java.math.BigDecimal;
import java.util.List;

public interface OrderManager {

    List<ExchangeOrder> getOpenOrders();

    void createOrder(OrderInstruction orderInstruction);

    void amendOrderQty(ExchangeOrder exchangeOrder, BigDecimal volume);

    void cancelOrder(ExchangeOrder exchangeOrder);
}
