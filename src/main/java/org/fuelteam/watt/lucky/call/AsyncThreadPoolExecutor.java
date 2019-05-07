package org.fuelteam.watt.lucky.call;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 类 AsyncThreadPoolExecutor
 *
 * @author Yin weihong (yinwh@gjmetal.com)
 *         <br>
 *         Copyright (c) 国金金属网
 */
@Component
public class AsyncThreadPoolExecutor implements FactoryBean<ThreadPoolExecutor> {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    private Integer corePoolSize = Runtime.getRuntime().availableProcessors();

    private Integer maximumPoolSize = 2 * corePoolSize + 1;

    private long keepAliveTime = 3;

    private TimeUnit unit = TimeUnit.SECONDS;

    private ThreadPoolExecutor executor;

    @Override
    public ThreadPoolExecutor getObject() {
        reentrantLock.lock();
        if (executor == null) {
            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(corePoolSize);
            RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
            executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }
        reentrantLock.unlock();
        return executor;
    }

    @Override
    public Class<ThreadPoolExecutor> getObjectType() {
        return ThreadPoolExecutor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}