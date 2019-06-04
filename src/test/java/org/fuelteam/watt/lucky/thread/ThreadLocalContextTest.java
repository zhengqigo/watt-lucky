package org.fuelteam.watt.lucky.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.fuelteam.watt.lucky.number.RandomUtil;
import org.fuelteam.watt.lucky.thread.Concurrents;
import org.fuelteam.watt.lucky.thread.ThreadLocalContext;
import org.fuelteam.watt.lucky.thread.ThreadUtil;
import org.junit.Test;

public class ThreadLocalContextTest {

    @Test
    public void test() throws InterruptedException {
        final CountDownLatch countdown = Concurrents.countDownLatch(10);
        final CyclicBarrier barrier = Concurrents.cyclicBarrier(10);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ThreadLocalContext.put("myname", Thread.currentThread().getName());
                ThreadUtil.sleep(RandomUtil.nextLong(100, 300));
                System.out.println((String) ThreadLocalContext.get("myname"));
                ThreadLocalContext.reset();
                System.out.println("shoud null for " + Thread.currentThread().getName() + ":" + ThreadLocalContext.get("myname"));
                countdown.countDown();
            }
        };

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
        countdown.await();
    }
}