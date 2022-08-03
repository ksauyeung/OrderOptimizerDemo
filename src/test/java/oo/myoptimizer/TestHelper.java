package oo.myoptimizer;

import oo.ExchangeOrder;
import oo.OrderInstruction;
import oo.Side;

import java.math.BigDecimal;

public class TestHelper {

    public static OrderInstruction getOrderInstructionImpl(final long qty, final Side side, final Double price) {

        return new OrderInstruction() {

            @Override
            public long getVolume() {
                return qty;
            }

            @Override
            public Side getSide() {
                return side;
            }

            @Override
            public BigDecimal getPrice() {
                if(price == null) {
                    return null;
                }
                return new BigDecimal(price);
            }
        };
    }


    public static ExchangeOrder getExchangeOrderImpl(final Side side, final Double price, final long qty, final long leaveQty) {

        return new ExchangeOrder() {

            @Override
            public Side getSide() {
                return side;
            }

            @Override
            public BigDecimal getPrice() {
                if(price == null) {
                    return null;
                }
                return new BigDecimal(price);
            }

            @Override
            public long getQuantity() {
                return qty;
            }

            @Override
            public long getLeavesQty() {
                return leaveQty;
            }
        };

    }


}
