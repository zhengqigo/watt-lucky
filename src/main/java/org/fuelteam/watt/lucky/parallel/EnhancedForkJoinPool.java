package org.fuelteam.watt.lucky.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class EnhancedForkJoinPool extends ForkJoinPool {

    public EnhancedForkJoinPool() {}

    public EnhancedForkJoinPool(int parallel) {
        super(parallel);
    }

    public EnhancedForkJoinPool(int parallel, ForkJoinWorkerThreadFactory factory,
            Thread.UncaughtExceptionHandler handler, boolean async) {
        super(parallel, factory, handler, async);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T invoke(ForkJoinTask<T> task) {
        if (!(task instanceof AbstractParallel)) return super.invoke(task);
        super.invoke(task);
        return (T) ((AbstractParallel<T>) task).getContext();
    }
}