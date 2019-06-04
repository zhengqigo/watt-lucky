package org.fuelteam.watt.lucky.time;

import java.util.Date;

public class ClockUtil {

    private static Clock instance = new DefaultClock();

    public static long elapsedTime(long beginTime) {
        return currentTimeMillis() - beginTime;
    }

    // 切换为DummyClock, 使用系统时间为初始时间, 单个测试完成后需要调用useDefaultClock()切换回去
    public static synchronized DummyClock useDummyClock() {
        instance = new DummyClock();
        return (DummyClock) instance;
    }

    // 切换为DummyClock, 单个测试完成后需要调用useDefaultClock()切换回去
    public static synchronized DummyClock useDummyClock(long timeStampMills) {
        instance = new DummyClock(timeStampMills);
        return (DummyClock) instance;
    }

    // 切换为DummyClock, 单个测试完成后需要调用useDefaultClock()切换回去
    public static synchronized DummyClock useDummyClock(Date date) {
        instance = new DummyClock(date);
        return (DummyClock) instance;
    }

    // 重置为默认Clock
    public static synchronized void useDefaultClock() {
        instance = new DefaultClock();
    }

    public static Date currentDate() {
        return instance.currentDate();
    }

    public static long currentTimeMillis() {
        return instance.currentTimeMillis();
    }

    // 操作系统启动到现在的纳秒数, 与系统时间是完全独立的两个时间体系
    public static long nanoTime() {
        return instance.nanoTime();
    }

    public interface Clock {

        Date currentDate();

        long currentTimeMillis();

        // 操作系统启动到现在的纳秒数
        long nanoTime();
    }

    public static class DefaultClock implements Clock {

        @Override
        public Date currentDate() {
            return new Date();
        }

        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        @Override
        public long nanoTime() {
            return System.nanoTime();
        }
    }

    public static class DummyClock implements Clock {

        private long time;
        private long nanoTme;

        public DummyClock() {
            this(System.currentTimeMillis());
        }

        public DummyClock(Date date) {
            this(date.getTime());
        }

        public DummyClock(long time) {
            this.time = time;
            this.nanoTme = System.nanoTime();
        }

        @Override
        public Date currentDate() {
            return new Date(time);
        }

        @Override
        public long currentTimeMillis() {
            return time;
        }

        @Override
        public long nanoTime() {
            return nanoTme;
        }

        public void updateNow(Date newDate) {
            time = newDate.getTime();
        }

        public void updateNow(long newTime) {
            this.time = newTime;
        }

        public void increaseTime(int millis) {
            time += millis;
        }

        public void decreaseTime(int millis) {
            time -= millis;
        }

        public void setNanoTime(long nanoTime) {
            this.nanoTme = nanoTime;
        }
    }
}