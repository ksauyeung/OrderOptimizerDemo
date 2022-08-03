package oo;

import java.util.List;

public interface OrderOptimiser {

    void optimise(List<OrderInstruction> orderInstructions, OrderManager orderManager);

}
