package oo.myoptimizer;

import oo.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyOrderOptimizerValidationTest {

    @Mock
    private OrderManager orderManagerMock;

    private MyOrderOptimizer myOrderOptimizerUnderTest;

    public MyOrderOptimizerValidationTest() {

        MockitoAnnotations.openMocks(this);
        myOrderOptimizerUnderTest = new MyOrderOptimizer();
    }

    @Test
    void OptimizeShouldThrowIllegalArgumentExceptionWhenOrderInstructionsIsNull() {

        List<OrderInstruction> orderInstructions = null;

        assertThrows(IllegalArgumentException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });
    }

    @Test
    void OptimizeShouldThrowIllegalArgumentExceptionWhenOrderManagerIsNull() {

        OrderManager orderManager = null;

        assertThrows(IllegalArgumentException.class, () -> {
            myOrderOptimizerUnderTest.optimise(List.of(), orderManager);
        });
    }

    @Test
    void OptimizeShouldThrowInvalidOrderInstructionExceptionWhenOrderInstructionHasNullPrice() {

        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(10, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(11, Side.Sell, null));

        assertThrows(InvalidOrderInstructionException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });

    }

    @Test
    void OptimizeShouldThrowInvalidOrderInstructionExceptionWhenOrderInstructionHasNegativePrice() {

        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(10, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(11, Side.Sell, -2.34));

        assertThrows(InvalidOrderInstructionException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });

    }

    @Test
    void OptimizeShouldThrowInvalidOrderInstructionExceptionWhenOrderInstructionHasNullSide() {

        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(10, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(11, null, 2.34));

        assertThrows(InvalidOrderInstructionException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });

    }

    @Test
    void OptimizeShouldThrowInvalidOrderInstructionExceptionWhenOrderInstructionHasNegativeQty() {

        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(10, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(-1, Side.Buy, 2.34));

        assertThrows(InvalidOrderInstructionException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });

    }

    @Test
    void OptimizeShouldThrowInvalidOrderInstructionExceptionWhenOrderInstructionHasZeroPrice() {
        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(10, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(8, Side.Sell, 0.0));

        assertThrows(InvalidOrderInstructionException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });
    }

    @Test
    void OptimizeShouldThrowInvalidOrderInstructionExceptionWhenOrderInstructionHasDuplicatedPrice() {
        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(10, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(8, Side.Sell, 1.23));

        assertThrows(InvalidOrderInstructionException.class, () -> {
            myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);
        });
    }
}