package org.fuelteam.watt.lucky.call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Async {

    @Autowired
    private AsyncThreadPoolExecutor asyncThreadPoolExecutor;

    public <T> void doAsync(final AbstractFunction<T> af) {
        asyncThreadPoolExecutor.getObject().submit(new Runnable() {
            @Override
            public void run() {
                Callback<T> cb = af.getCallback();
                try {
                    T t = af.execute();
                    if (cb != null) cb.onSuccess(t);
                } catch (Exception ex) {
                    if (cb != null) cb.onFailure(ex);
                }
            }
        });
    }
}