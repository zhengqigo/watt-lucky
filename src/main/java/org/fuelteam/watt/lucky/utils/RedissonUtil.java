package org.fuelteam.watt.lucky.utils;

import java.util.concurrent.ExecutionException;

import org.fuelteam.watt.lucky.utils.StringUtil;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedissonUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(RedissonUtil.class);
    
    public static String getAsync(RedissonClient redissonClient, String bucket) {
        RFuture<Object> future = redissonClient.getBucket(bucket).getAsync();
        Object result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException ex) {
            logger.error("getAsync exception", ex);
        }
        return StringUtil.clean(result);
    }
}
