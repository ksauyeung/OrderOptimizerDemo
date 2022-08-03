package oo.myoptimizer;

import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.ConcurrentTestRunner;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;
import oo.ExchangeOrder;
import oo.OrderInstruction;
import oo.OrderManager;
import oo.Side;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(ConcurrentTestRunner.class)
public class MyOrderOptimizerConcurrentTest {
    @Rule
    public ConcurrentRule concurrentRule= new ConcurrentRule();
    @Rule
    public RepeatingRule repeatingRule = new RepeatingRule();

    static int cancelCount = 0;
    static int amendCount = 0;
    static int createCount = 0;

    private OrderManager orderManagerImpl = new OrderManager() {

        ExchangeOrder openOrder = TestHelper.getExchangeOrderImpl(Side.Buy, 1.230, 100, 100);

        @Override
        public List<ExchangeOrder> getOpenOrders() {
            return List.of(openOrder);
        }

        @Override
        public void createOrder(OrderInstruction orderInstruction) {
            createCount++;
        }

        @Override
        public void amendOrderQty(ExchangeOrder exchangeOrder, BigDecimal volume) {
            amendCount++;
        }

        @Override
        public void cancelOrder(ExchangeOrder exchangeOrder) {
            cancelCount++;
        }
    };
    private volatile MyOrderOptimizerV2 myOrderOptimizerUnderTest = new MyOrderOptimizerV2();

    @Test
    @Concurrent(count = 100)
    @Repeating(repetition = 100)
    public void OptimizeShouldNotCallCancelConcurrently() {
        List<OrderInstruction> orderInstructions = List.of();
        myOrderOptimizerUnderTest.optimise(orderInstructions, orderManagerImpl);
    }

    @Test
    @Concurrent(count = 100)
    @Repeating(repetition = 100)
    public void OptimizeShouldNotCallCreateConcurrently() {
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(12, Side.Buy, 2.34);
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerImpl);
    }

    @Test
    @Concurrent(count = 100)
    @Repeating(repetition = 100)
    public void OptimizeShouldNotCallAmendConcurrently() {
        OrderInstruction orderInstruction = TestHelper.getOrderInstructionImpl(12, Side.Buy, 1.230);
        myOrderOptimizerUnderTest.optimise(List.of(orderInstruction), orderManagerImpl);
    }

    @AfterClass
    public static void after() {
        System.out.println("CreateCount is " + createCount);
        Assert.assertEquals(10000, createCount);
        System.out.println("AmendCount is " + amendCount);
        Assert.assertEquals(10000, amendCount);
        System.out.println("CancelCount is " + cancelCount);
        Assert.assertEquals(20000, cancelCount);
    }
}
