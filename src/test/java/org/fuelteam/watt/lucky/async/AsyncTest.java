package org.fuelteam.watt.lucky.async;

import org.junit.Assert;
import org.junit.Test;

public class AsyncTest {

    @Test
    public void test() {
        Integer result = 1;
        Callback<Integer> cb = new Callback<Integer>() {
            @Override
            public void onSuccess(Integer t) {
                Assert.assertTrue(result.equals(t));
            }

            @Override
            public void onFailure(Exception ex) {
                Assert.assertTrue(ex == null);
            }
        };
        Async.doAsync(new AbstractFunction<Integer>(cb) {
            @Override
            public Integer execute() throws Exception {
                return result;
            }
        });
    }
}