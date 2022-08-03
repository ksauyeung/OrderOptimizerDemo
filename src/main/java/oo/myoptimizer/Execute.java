package oo.myoptimizer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Execute {
    private static final ExecutorService EXECUTION_SERVICE = Executors.newFixedThreadPool(4);

    public static ExecutorService getExecutor() {
        return EXECUTION_SERVICE;
    }

    public static void immediateExecution(final Runnable command_) {
        EXECUTION_SERVICE.execute(command_);
    }
}
