package com.spring.redis.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

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

            return jsonToObject(str, clazz);
        }
    }

    /**
     * set value with key
     */
    public <T> boolean set(String key, T value, int expireSeconds) {
        try (Jedis resource = jedisPool.getResource()) {
            String valueStr = objectToJson(value);
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

    private <T> T jsonToObject(String json, Class<T> clazz) {
        return JsonAndXmlUtils.jsonToObject(json, clazz);
    }

    private String objectToJson(Object object) {
        return JsonAndXmlUtils.objectToJson(object);
    }

    private Map jsonToMap(String json) {
        return JsonAndXmlUtils.jsonToMap(json);
    }

    private String objectToXml(Object data) {
        return JsonAndXmlUtils.objectToXml(data);
    }

    private <T> T xmlToObject(String xml, Class<T> clazz) {
        return JsonAndXmlUtils.xmlToObject(xml, clazz);
    }

    private String mapToXml(Map<String, Object> map) {
        return JsonAndXmlUtils.mapToXml(map);
    }

}
