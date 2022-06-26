package cn.fan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/26
 * @Created by fanduanjin
 */
@SpringBootApplication
@EnableScheduling
public class ServerScheduledTask {
    public static void main(String[] args) {
        SpringApplication.run(ServerScheduledTask.class,args);
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
