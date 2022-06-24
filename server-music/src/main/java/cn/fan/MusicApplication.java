package cn.fan;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/17
 * @Created by fanduanjin
 */
@SpringBootApplication
public class MusicApplication {


    @Autowired
    public ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MusicApplication.class, args);
        System.out.println("启动完毕");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
