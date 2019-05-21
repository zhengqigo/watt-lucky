package org.fuelteam.watt.lucky.time;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @see https://github.com/apache/logging-log4j2/blob/master/log4j-core/src/main/java/org/apache/logging/log4j/core/pattern/DatePatternConverter.java#L272
 */
public class CachingDateFormatter {
    private FastDateFormat fastDateFormat;
    private AtomicReference<CachedTime> cachedTime;
    private boolean onSecond;

    public CachingDateFormatter(String pattern) {
        this(FastDateFormat.getInstance(pattern));
    }

    public CachingDateFormatter(FastDateFormat fastDateFormat) {
        this.fastDateFormat = fastDateFormat;
        onSecond = fastDateFormat.getPattern().indexOf("SSS") == -1;
        long current = System.currentTimeMillis();
        this.cachedTime = new AtomicReference<CachedTime>(new CachedTime(current, fastDateFormat.format(current)));
    }

    public String format(final long timestampMillis) {
        CachedTime cached = cachedTime.get();
        long timestamp = onSecond ? timestampMillis / 1000 : timestampMillis;
        if (timestamp != cached.timestamp) {
            final CachedTime newCachedTime = new CachedTime(timestamp, fastDateFormat.format(timestampMillis));
            cachedTime.compareAndSet(cached, newCachedTime);
            cached = newCachedTime;
        }
        return cached.formatted;
    }

    static final class CachedTime {
        public long timestamp;
        public String formatted;

        public CachedTime(final long timestamp, String formatted) {
            this.timestamp = timestamp;
            this.formatted = formatted;
        }
    }
}