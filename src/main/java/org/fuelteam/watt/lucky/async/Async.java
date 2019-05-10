package org.fuelteam.watt.lucky.async;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.fuelteam.watt.lucky.base.RuntimeUtil;
import org.fuelteam.watt.lucky.concurrent.threadpool.ThreadPoolBuilder;

/**
 * 异步调用类
 */
public class Async {

    private final static Integer cores = RuntimeUtil.getCores();

    private final static RejectedExecutionHandler executionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

    private final static ThreadPoolExecutor threadPoolExecutor = ThreadPoolBuilder.cachedPool().setMaxSize(2 * cores + 1)
            .setMinSize(cores).setKeepAliveSecs(10).setRejectHanlder(executionHandler).build();

    public static <T> void doAsync(final AbstractFunction<T> af) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Callback<T> cb = af.getCallback();
                try {
                    T t = af.execute();
                    if (cb != null) cb.onSuccess(t);
                } catch (Exception ex) {
                    if (cb != null) cb.onFailure(ex);
                }
            }
        };
        threadPoolExecutor.submit(runnable);
    }
}