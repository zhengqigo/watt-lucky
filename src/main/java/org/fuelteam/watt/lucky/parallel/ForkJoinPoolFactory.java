package org.fuelteam.watt.lucky.parallel;

public class ForkJoinPoolFactory {

    private int parallel;

    private EnhancedForkJoinPool enhancedForkJoinPool;

    public ForkJoinPoolFactory() {
        this(Runtime.getRuntime().availableProcessors() * 16);
    }

    public ForkJoinPoolFactory(int parallel) {
        this.parallel = parallel;
        enhancedForkJoinPool = new EnhancedForkJoinPool(parallel);
    }

    public EnhancedForkJoinPool getObject() {
        return this.enhancedForkJoinPool;
    }

    public int getParallel() {
        return parallel;
    }

    public void setParallel(int parallel) {
        this.parallel = parallel;
    }

    public void destroy() throws Exception {
        this.enhancedForkJoinPool.shutdown();
    }
}