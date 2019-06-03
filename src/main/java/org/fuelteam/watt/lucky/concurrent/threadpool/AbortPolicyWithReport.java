package org.fuelteam.watt.lucky.concurrent.threadpool;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.fuelteam.watt.lucky.concurrent.ThreadDumpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private static final Logger logger = LoggerFactory.getLogger(AbortPolicyWithReport.class);

    private final String threadName;

    private ThreadDumpper dummper = new ThreadDumpper();

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String message = "Thread pool is EXHAUSTED! Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), "
                + "Task: %d (completed: %d), Executor status: (isShutdown:%s, isTerminated:%s, isTerminating:%s)!";
        String msg = String.format(message, threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(),
                e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(),
                e.isTerminated(), e.isTerminating());
        logger.warn(msg);
        dummper.tryThreadDump(null);
        throw new RejectedExecutionException(msg);
    }
}