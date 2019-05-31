package org.fuelteam.watt.lucky.concurrent.limiter;

import org.apache.commons.lang3.Validate;
import org.fuelteam.watt.lucky.number.RandomUtil;

/**
 * 从Twitter Common移植的采样器，优化使用ThreadLocalRandom
 * <pre>
 * https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/util/Sampler.java
 */
public class Sampler {

    private static final Double ALWAYS = Double.valueOf(100);
    private static final Double NEVER = Double.valueOf(0);

    private double threshold;

    protected Sampler() {}

    protected Sampler(double selectPercent) {
        Validate.isTrue((selectPercent >= 0) && (selectPercent <= 100), "Invalid selectPercent value: " + selectPercent);
        this.threshold = selectPercent / 100;
    }

    public static Sampler create(Double selectPercent) {
        if (selectPercent.equals(ALWAYS)) return new AlwaysSampler();
        if (selectPercent.equals(NEVER)) return new NeverSampler();
        return new Sampler(selectPercent);
    }

    // 判断当前请求是否命中采样
    public boolean select() {
        return RandomUtil.threadLocalRandom().nextDouble() < threshold;
    }

    // 采样率为100时总返回true
    protected static class AlwaysSampler extends Sampler {
        @Override
        public boolean select() {
            return true;
        }
    }

    // 采样率为0时总返回false
    protected static class NeverSampler extends Sampler {
        @Override
        public boolean select() {
            return false;
        }
    }
}