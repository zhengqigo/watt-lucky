package org.fuelteam.watt.lucky.async;

import org.fuelteam.watt.lucky.utils.Vardump;
import org.junit.Test;

public class AsyncTest {

    @Test
    public void test() {
        Callback<Integer> cb = new Callback<Integer>() {
            @Override
            public void onSuccess(Integer t) {
                Vardump.print("onSuccess");
            }

            @Override
            public void onFailure(Exception ex) {
                Vardump.print("onFailure");
            }
        };
        Async.doAsync(new AbstractFunction<Integer>(cb) {
            @Override
            public Integer execute() throws Exception {
                return 1;
            }
        });
    }
}