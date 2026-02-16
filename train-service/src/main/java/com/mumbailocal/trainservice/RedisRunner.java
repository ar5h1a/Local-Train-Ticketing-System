package com.mumbailocal.trainservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mumbailocal.trainservice.service.RedisTestService;

@Component
public class RedisRunner implements CommandLineRunner {

    private final RedisTestService redisTestService;

    public RedisRunner(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @Override
    public void run(String... args) {
        redisTestService.testRedis();
    }
}
