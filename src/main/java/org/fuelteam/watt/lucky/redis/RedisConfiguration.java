package org.fuelteam.watt.lucky.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisPassword redisPassword = RedisPassword.of(redisProperties.getPassword());
        JedisConnectionFactory jcf;
        if (redisProperties.isClusterMode()) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(redisProperties.getMaxTotal());
            poolConfig.setMaxIdle(redisProperties.getMaxIdle());
            poolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
            poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
            poolConfig.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMillis());
            poolConfig.setSoftMinEvictableIdleTimeMillis(redisProperties.getSoftMinEvictableIdleTimeMillis());
            poolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
            poolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
            poolConfig.setTestOnCreate(redisProperties.isTestOnCreate());
            poolConfig.setTestOnReturn(redisProperties.isTestOnReturn());
            poolConfig.setTestWhileIdle(redisProperties.isTestWhileIdle());
            poolConfig.setBlockWhenExhausted(redisProperties.isBlockWhenExhausted());
            jcf = new JedisConnectionFactory(new RedisClusterConfiguration(redisProperties.getNodes()), poolConfig);
        } else {
            RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration(redisProperties.getHost(),
                    redisProperties.getPort());
            standalone.setPassword(redisPassword);
            jcf = new JedisConnectionFactory(standalone);
        }
        return jcf;
    }

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate() {
        final RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        this.init(redisTemplate, redisConnectionFactory());
        return redisTemplate;
    }

    private <T> void init(RedisTemplate<String, T> redisTemplate, RedisConnectionFactory factory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
    }
}
