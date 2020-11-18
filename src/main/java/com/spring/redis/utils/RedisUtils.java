package com.spring.redis.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: common-spring-boot-starter
 * @package com.spring.common.utils
 * @description
 * @author: XuZhenkui
 * @create: 2020-11-18 11:27
 **/
@Component
public class RedisUtils {
    private final JedisPool jedisPool;

    public RedisUtils(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    /**
     * get value with key
     */
    public <T> T get(String key, Class<T> clazz) {
        try (Jedis resource = jedisPool.getResource()) {
            String str = resource.get(key);

            return stringToBean(str, clazz);
        }
    }

    /**
     * set value with key
     */
    public <T> boolean set(String key, T value, int expireSeconds) {
        try (Jedis resource = jedisPool.getResource()) {
            String valueStr = beanToString(value);
            if (valueStr == null || valueStr.length() == 0) {
                return false;
            }

            if (expireSeconds <= 0) {
                resource.set(key, valueStr);
            } else {
                resource.setex(key, expireSeconds, valueStr);
            }

            return true;
        }
    }

    private <T> T stringToBean(String str, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(str, clazz);
    }

    private <T> String beanToString(T value) {
        Gson gson = new Gson();
        return gson.toJson(value);
    }
}
