package org.fuelteam.watt.lucky.parallel;

import java.util.List;
import java.util.concurrent.ForkJoinTask;

import com.google.common.collect.Lists;

public class DefaultForkJoinParallel<T> extends AbstractParallel<T> {

    private static final long serialVersionUID = 2928344256416319437L;

    private List<AbstractParallel<T>> parallelList;

    public DefaultForkJoinParallel(T context) {
        super(context);
        parallelList = Lists.newArrayList();
    }

    public DefaultForkJoinParallel<T> addParallel(IParallel<T> parallel) {
        parallelList.add(new AbstractParallel<T>(this.context) {
            private static final long serialVersionUID = -3249086748748181563L;

            @Override
            public void execute(T context) {
                parallel.execute(context);
            }
        });
        return this;
    }

    @Override
    public void execute(T context) {
        this.parallelList.forEach(ForkJoinTask::fork);
    }

    @Override
    public T getContext() {
        this.parallelList.forEach(ForkJoinTask::join);
        return this.context;
    }
}