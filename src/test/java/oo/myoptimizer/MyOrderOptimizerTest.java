package oo.myoptimizer;

import oo.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


public class MyOrderOptimizerTest {

    @Mock
    private OrderManager orderManagerMock;

    private MyOrderOptimizerV2 myOrderOptimizerUnderTest;

    public MyOrderOptimizerTest() {
        MockitoAnnotations.openMocks(this);
        myOrderOptimizerUnderTest = new MyOrderOptimizerV2();
    }

    @Test
    void OptimizeShouldCallCancelOrderWhenOpenPriceLevelNotInOrderInstructions() {
        //given
        ExchangeOrder openOrder = Mockito.mock(ExchangeOrder.class);
        Mockito.when(openOrder.getPrice()).thenReturn(new BigDecimal(1.2300));
        Mockito.when(openOrder.getSide()).thenReturn(Side.Buy);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));
        List<OrderInstruction> orderInstructions = List.of();

        //when
        myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(1)).cancelOrder(openOrder);
    }

    @Test
    void OptimizeShouldCallOnlyCancelOrderWhenOpenPriceLevelInOrderInstructionsAndNewQtyIsZero() {
        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 6);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(0, Side.Buy, 1.23);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(1)).cancelOrder(openOrder);
        verify(orderManagerMock, Mockito.times(0)).createOrder(any(OrderInstruction.class));
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
    }

    @Test
    void OptimizeShouldNotCallCreateOrderWhenGetOpenOrderReturnsNull() {
        //given
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(5, Side.Buy, 1.23);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(null);

        //when
        try {
            myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);
        } catch (Exception e) {}

        //then
        verify(orderManagerMock, Mockito.times(0)).createOrder(any(OrderInstruction.class));
    }

    @Test
    void OptimizeShouldCallCreateOrderWhenOrderInstructionIsNewPriceLevel() {

        //given
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(12, Side.Buy, 2.34);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of());

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(0)).cancelOrder(any(ExchangeOrder.class));
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
        verify(orderManagerMock, Mockito.times(1)).createOrder(orderInstruction);
    }

    @Test
    void OptimizeShouldCallCreateOrderAfterCancelOrderWhenOpenPriceLevelInOrderInstructionsAndNewQtyEqualToExecutedQty() {

        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 6);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(6, Side.Buy, 1.23);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));
        InOrder inOrder = Mockito.inOrder(orderManagerMock);

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
        inOrder.verify(orderManagerMock).cancelOrder(openOrder);
        inOrder.verify(orderManagerMock).createOrder(orderInstruction);
    }

    @Test
    void OptimizeShouldCallCreateOrderAfterCancelOrderWhenSideIsDifferentForSamePrice() {

        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 12);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(12, Side.Sell, 1.23);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));
        InOrder inOrder = Mockito.inOrder(orderManagerMock);

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
        inOrder.verify(orderManagerMock).cancelOrder(openOrder);
        inOrder.verify(orderManagerMock).createOrder(orderInstruction);
    }

    @Test
    void OptimizeShouldCallCreateOrderAfterCancelOrderWhenOpenPriceLevelInOrderInstructionsAndNewQtyLessThanExecutedQty() {
        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 6);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(5, Side.Buy, 1.23);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));
        InOrder inOrder = Mockito.inOrder(orderManagerMock);

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
        inOrder.verify(orderManagerMock).cancelOrder(openOrder);
        inOrder.verify(orderManagerMock).createOrder(orderInstruction);
    }

    @Test
    void OptimizeShouldNotCallCreateOrderAfterCancelOrderThrewExceptionWhenOpenPriceLevelInOrderInstructionsAndNewQtyLessThanExecutedQty() {

        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 6);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(5, Side.Buy, 1.23);
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));
        doThrow(new RuntimeException()).when(orderManagerMock).cancelOrder(any(ExchangeOrder.class));

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //expect
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
        verify(orderManagerMock, Mockito.times(1)).cancelOrder(openOrder);
        verify(orderManagerMock, Mockito.times(0)).createOrder(any(OrderInstruction.class));
    }

    @Test
    void OptimizeShouldCallOnlyAmendOrderWhenOpenPriceLevelInOrderInstructionsAndNewQtyIsDifferent() {

        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 12);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(13, Side.Buy, 1.23);

        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(0)).cancelOrder(any(ExchangeOrder.class));
        verify(orderManagerMock, Mockito.times(0)).createOrder(any(OrderInstruction.class));
        verify(orderManagerMock, Mockito.times(1)).amendOrderQty(openOrder, BigDecimal.valueOf(orderInstruction.getVolume()));
    }

    @Test
    void OptimizeShouldDoNothingWhenOpenPriceLevelInOrderInstructionsAndSideAndNewQtyAreSame() {

        //given
        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.23, 12, 5);
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(12, Side.Buy, 1.23);

        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(List.of(openOrder));

        //when
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerMock);

        //then
        verify(orderManagerMock, Mockito.times(0)).cancelOrder(any(ExchangeOrder.class));
        verify(orderManagerMock, Mockito.times(0)).createOrder(any(OrderInstruction.class));
        verify(orderManagerMock, Mockito.times(0)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));
    }

    @Test
    void OptimizeShouldCallCreateOrAmendForAllOrderInstructions() {

        //given
        List<ExchangeOrder> openOrders = List.of(
                TestHelper.getExchangeOrderImpl(Side.Sell, 1.23, 11, 11),  //amend down
                TestHelper.getExchangeOrderImpl(Side.Buy, 2.34, 1, 1),    //amend up
                TestHelper.getExchangeOrderImpl(Side.Sell, 3.45, 4, 1),   //delete new
                TestHelper.getExchangeOrderImpl(Side.Buy, 4.568, 7, 7));  //delete

        List<OrderInstruction> orderInstructions = List.of(
                TestHelper.getOrderInstructionImpl(1, Side.Sell, 1.23),
                TestHelper.getOrderInstructionImpl(2, Side.Buy, 2.34),
                TestHelper.getOrderInstructionImpl(3, Side.Sell, 3.45),
                TestHelper.getOrderInstructionImpl(4, Side.Buy, 4.567));          //new
        Mockito.when(orderManagerMock.getOpenOrders()).thenReturn(openOrders);

        //when
        myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerMock);

        //expects
        verify(orderManagerMock, Mockito.times(2)).createOrder(any(OrderInstruction.class));
        verify(orderManagerMock, Mockito.times(2)).amendOrderQty(any(ExchangeOrder.class), any(BigDecimal.class));

    }
}