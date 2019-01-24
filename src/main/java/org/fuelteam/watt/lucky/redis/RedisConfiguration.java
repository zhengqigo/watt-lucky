package org.fuelteam.watt.lucky.redis;

import java.util.LinkedHashSet;
import java.util.Set;

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

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    private JedisPoolConfig jedisPoolConfig() {
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
        return poolConfig;
    }

    @Bean
    public JedisCluster jedisCluster() {
        if (!redisProperties.isClusterMode()) return null;
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        for (String str : redisProperties.getNodes()) {
            String[] array = str.split(":");
            nodes.add(new HostAndPort(array[0], Integer.parseInt(array[1])));
        }
        JedisCluster jedisCluster = new JedisCluster(nodes, redisProperties.getConnectionTimeout(),
                redisProperties.getSoTimeout(), redisProperties.getMaxAttempts(), redisProperties.getPassword(),
                jedisPoolConfig());
        return jedisCluster;
    }

    @Bean
    public JedisPool jedisPool() {
        if (redisProperties.isClusterMode()) return null;
        JedisPool jedisPool = new JedisPool(jedisPoolConfig(), redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getTimeout(), redisProperties.getPassword());
        return jedisPool;
    }

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate() {
        final RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private RedisConnectionFactory redisConnectionFactory() {
        RedisPassword redisPassword = RedisPassword.of(redisProperties.getPassword());
        JedisConnectionFactory jcf;
        if (redisProperties.isClusterMode()) {
            jcf = new JedisConnectionFactory(new RedisClusterConfiguration(redisProperties.getNodes()), jedisPoolConfig());
        } else {
            RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration(redisProperties.getHost(),
                    redisProperties.getPort());
            standalone.setPassword(redisPassword);
            jcf = new JedisConnectionFactory(standalone);
        }
        return jcf;
    }
}
