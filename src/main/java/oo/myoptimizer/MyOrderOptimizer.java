package oo.myoptimizer;

import oo.ExchangeOrder;
import oo.OrderInstruction;
import oo.OrderManager;
import oo.OrderOptimiser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderOptimizer implements OrderOptimiser {

    @Override
    public void optimise(List<OrderInstruction> orderInstructions, OrderManager orderManager) {

        final Map<PriceLevel, OrderInstruction> orderInstructionsMap = validateAndCollect(orderInstructions, orderManager);

        synchronized(orderManager) {

            // loop open orders
            //  if open price is not in orderInstructionsMap, delete order
            //  if open price is in orderInstructionsMap,
            //   check if new qty =0, delete
            //   check if new qty is <= executedQty, delete, and create
            //   check if new qty is diff, update
            //  remove price point from orderInstructionsMap
            // loop the orderInstructionsMap and post new order

            List<ExchangeOrder> openOrders = null;
            try {
                openOrders = orderManager.getOpenOrders();
            } catch (Exception e) {
                Util.log(e);
            }
            if(openOrders == null) {
                throw new OpenOrdersException("Unable to get open orders.");
            }

            for(ExchangeOrder openOrder : openOrders) {

                final PriceLevel openOrderPriceLevel = new PriceLevel(openOrder);
                final OrderInstruction oi = orderInstructionsMap.get(openOrderPriceLevel);

                if(oi == null || oi.getVolume() == 0) {
                    Dispatcher.dispatch(openOrder, () -> {
                        orderManager.cancelOrder(openOrder);
                    });

                } else {

                    final Long executedQty = openOrder.getQuantity() - openOrder.getLeavesQty();
                    if(oi.getSide() != openOrder.getSide() || oi.getVolume() <= executedQty ) {
                        //side changed, or the entire new amount has already executed, cancel the order, and replenish with a new one
                        Dispatcher.dispatch(openOrder, () -> {
                            orderManager.cancelOrder(openOrder);
                            orderManager.createOrder(oi);
                        });

                    } else if (oi.getVolume() != openOrder.getQuantity()) {
                        Dispatcher.dispatch(openOrder, () -> {
                            orderManager.amendOrderQty(openOrder, BigDecimal.valueOf(oi.getVolume()));
                        });

                    } else {
                        Util.log(String.format("Order qty unchanged for %s %.8f", openOrder.getSide(), openOrder.getPrice()));
                    }

                }
                orderInstructionsMap.remove(openOrderPriceLevel);

            }

            for(OrderInstruction oi : orderInstructionsMap.values()) {
                Dispatcher.dispatch(() -> {
                    orderManager.createOrder(oi);
                });
            }
        }
    }

    /**
     * Validates inputs and returns a map of the order instructions
     * @param orderInstructions
     * @param orderManager
     * @return If all order instructions are valid, a hash map of the order instructions keyed by price, or null otherwise
     * @apiNote Throws runtime exception if any inputs is invalid
     */
    private Map<PriceLevel, OrderInstruction> validateAndCollect(List<OrderInstruction> orderInstructions, OrderManager orderManager) {
        if(orderInstructions == null) {
            throw new IllegalArgumentException("orderInstructions cannot be null.");
        }
        if(orderManager == null) {
            throw new IllegalArgumentException("orderManager cannot be null.");
        }

        final HashMap<PriceLevel, OrderInstruction> orderInstructionsMap;
        int invalids = 0;

        synchronized (orderInstructions) {
            orderInstructionsMap = new HashMap<>(orderInstructions.size());
            for(OrderInstruction oi : orderInstructions) {
                Util.log(String.format("Validating %d order instructions...", orderInstructions.size()));

                if(!validateOrderInstruction(oi)) {
                    invalids++;
                }

                if(invalids == 0) {
                    if(orderInstructionsMap.put(new PriceLevel(oi), oi) != null) {
                        Util.log(String.format("Duplicated price level %.8f found in orderInstructions", oi.getPrice()));
                        invalids++;
                    }
                }
            }
        }
        if(invalids > 0) {
            Util.log(String.format("There are %d invalid order instructions. Operation cancelled.", invalids));
            throw new InvalidOrderInstructionException("One or more orders have invalid instructions");
        }

        return orderInstructionsMap;
    }

    private boolean validateOrderInstruction(OrderInstruction orderInstruction) {

        String msg = null;
        if(orderInstruction == null) {
            msg = "orderInstructions is null";
        }

        else if(orderInstruction.getPrice() == null) {
            msg = "orderInstructions price is null";
        }

        else if(orderInstruction.getPrice().signum() <= 0) {
            msg = String.format("orderInstructions %.8f price is less than zero", orderInstruction.getPrice());
        }

        else if(orderInstruction.getSide() == null) {
            msg = String.format("orderInstructions %.8f side is null", orderInstruction.getPrice());
        }

        else if(orderInstruction.getVolume() < 0) {
            msg = String.format("orderInstructions %.8f volume is less than zero", orderInstruction.getPrice());
        }

        if(msg != null) {
            Util.log(msg);
            return false;
        }

        return true;

    }
}
