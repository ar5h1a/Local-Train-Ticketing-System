package com.mumbailocal.trainservice.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void testRedis() {
        redisTemplate.opsForValue().set("testKey", "Hello Redis");
        Object value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis value = " + value);
    }
}
