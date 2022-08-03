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
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

@RunWith(ConcurrentTestRunner.class)
public class MyTest {

    @Test
    public void test() {
        System.out.println("oh its nice weather today");
        Assert.assertEquals(0, 0);
    }

}
