package org.fuelteam.watt.lucky.parallel;

import java.util.concurrent.RecursiveAction;

public abstract class AbstractParallel<T> extends RecursiveAction implements IParallel<T> {

    private static final long serialVersionUID = -7130485686073836674L;

    protected T context;

    public AbstractParallel(T context) {
        this.context = context;
    }

    @Override
    public void compute() {
        execute(context);
    }

    public T getContext() {
        this.join();
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }
}