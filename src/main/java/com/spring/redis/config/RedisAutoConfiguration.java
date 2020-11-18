package com.spring.redis.config;

import com.spring.redis.properties.RedisProperties;
import com.spring.redis.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: common-spring-boot-starter
 * @package com.spring.common.config
 * @description
 * @author: XuZhenkui
 * @create: 2020-11-18 11:30
 **/
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {
    private final static Logger logger = LoggerFactory.getLogger(RedisAutoConfiguration.class);

    private final RedisProperties properties;

    public RedisAutoConfiguration(RedisProperties properties) {
        this.properties = properties;
    }

    @Bean
    // 表示当Spring容器中没有JedisPool类的对象时，才调用该方法
    @ConditionalOnMissingBean(JedisPool.class)
    public JedisPool jedisPool() {
        logger.info("redis connect string: {}:{}", properties.getHost(), properties.getPort());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(properties.getMaxIdle());
        jedisPoolConfig.setMaxTotal(properties.getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(properties.getMaxWaitMillis());

        String password = properties.getPassword();
        if (password == null || password.length() == 0) {
            return new JedisPool(jedisPoolConfig, properties.getHost(),
                    properties.getPort(), properties.getTimeout());
        }

        return new JedisPool(jedisPoolConfig, properties.getHost(),
                properties.getPort(), properties.getTimeout(), properties.getPassword());
    }

    @Bean
    @ConditionalOnMissingBean(RedisUtils.class)
    public RedisUtils redisUtils(JedisPool jedisPool){
        return new RedisUtils(jedisPool);
    }
}
