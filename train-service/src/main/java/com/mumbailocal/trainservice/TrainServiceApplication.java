package com.mumbailocal.trainservice;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaching
public class TrainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainServiceApplication.class, args);
    }
}
