package org.fuelteam.watt.lucky.call;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component("asyncThreadPoolExecutor")
public class AsyncThreadPoolExecutor implements FactoryBean<ThreadPoolExecutor> {

    private final ReentrantLock lock = new ReentrantLock();

    private Integer minCores = Runtime.getRuntime().availableProcessors();

    private Integer maxCores = 2 * minCores + 1;

    private long aliveTime = 3;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private ThreadPoolExecutor executor;

    public ThreadPoolExecutor getObject() {
        lock.lock();
        if (executor == null) {
            BlockingQueue<Runnable> bq = new LinkedBlockingQueue<Runnable>(minCores);
            RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
            executor = new ThreadPoolExecutor(minCores, maxCores, aliveTime, timeUnit, bq, handler);
        }
        lock.unlock();
        return executor;
    }

    public Class<ThreadPoolExecutor> getObjectType() {
        return ThreadPoolExecutor.class;
    }

    public boolean isSingleton() {
        return true;
    }
}