package oo.myoptimizer;

import oo.ExchangeOrder;

import java.util.concurrent.CountDownLatch;

public class Dispatcher {

    public static void dispatch(ExchangeOrder order, Block block) {
        if(block == null) {
            return;
        }
        if (order != null) {
            Execute.immediateExecution(() -> {
                    try {
                        synchronized(order) {
                            block.block();
                        }
                    } catch (Exception e) {
                        Util.log(String.format("Exception on dispatch: %s %f", order.getSide(), order.getPrice()), e);
                    }
                });
        }
    }

    public static void dispatch(Block block) {
        if(block == null) {
            return;
        }
        Execute.immediateExecution(() -> {
            try {
                block.block();
            } catch (Exception e) {
                Util.log("Exception on no sync dispatch", e);
            }
        });
    }

    public static void dispatchWithCountDown(Block block, CountDownLatch cdl) {
        if(block == null) {
            if(cdl != null) {
                cdl.countDown();
            }
            return;
        }
        Execute.immediateExecution(() -> {
            try {
                block.block();
            } catch (Exception e) {
                Util.log("Exception on no sync dispatch", e);
            } finally {
                if(cdl != null) {
                    cdl.countDown();
                }
            }
        });
    }

}
