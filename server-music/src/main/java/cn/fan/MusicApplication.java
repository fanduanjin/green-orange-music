package cn.fan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.zookeeper.config.ZookeeperPropertySourceLocator;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.Entity;
import java.io.IOException;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/17
 * @Created by fanduanjin
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class MusicApplication {


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
