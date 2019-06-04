package org.fuelteam.watt.lucky.thread;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.Validate;

/**
 * Future实现类的基本框架
 * @see https://github.com/apache/httpcomponents-core/blob/master/httpcore5/src/main/java/org/apache/hc/core5/concurrent/BasicFuture.java
 */
public abstract class BasicFuture<T> implements Future<T> {

    private volatile boolean completed; // NOSONAR
    private volatile boolean cancelled;
    private volatile T result;
    private volatile Exception ex;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isDone() {
        return this.completed;
    }

    @Override
    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!this.completed) {
            wait();
        }
        return getResult();
    }

    @Override
    public synchronized T get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Validate.notNull(unit, "Time unit");
        final long msecs = unit.toMillis(timeout);
        final long startTime = (msecs <= 0) ? 0 : System.currentTimeMillis();
        long waitTime = msecs;
        if (this.completed) return getResult();
        if (waitTime <= 0) throw new TimeoutException();
        for (;;) {
            wait(waitTime);
            if (this.completed) return getResult();
            waitTime = msecs - (System.currentTimeMillis() - startTime);
            if (waitTime <= 0) throw new TimeoutException();
        }
    }

    private T getResult() throws ExecutionException {
        if (this.ex != null) throw new ExecutionException(this.ex);
        if (cancelled) throw new CancellationException();
        return this.result;
    }

    public boolean completed(final T result) {
        synchronized (this) {
            if (this.completed) return false;
            this.completed = true;
            this.result = result;
            notifyAll();
        }
        onCompleted(result);
        return true;
    }

    public boolean failed(final Exception exception) {
        synchronized (this) {
            if (this.completed) return false;
            this.completed = true;
            this.ex = exception;
            notifyAll();
        }
        onFailed(exception);
        return true;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (this.completed) return false;
            this.completed = true;
            this.cancelled = true;
            notifyAll();
        }
        onCancelled();
        return true;
    }

    protected abstract void onCompleted(T result);

    protected abstract void onFailed(Exception ex);

    protected abstract void onCancelled();
}