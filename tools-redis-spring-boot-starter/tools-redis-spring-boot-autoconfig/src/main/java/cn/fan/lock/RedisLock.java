package cn.fan.lock;

import com.sun.corba.se.impl.orbutil.LogKeywords;
import io.lettuce.core.cluster.RedisClusterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author fanduanjin
 * @Description 基于redis实现分布式锁  !!!不支持迭代
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
@Component
public class RedisLock {
    /**
     * key 存活时期
     */
    private static final Duration TIMEOUT = Duration.ofMinutes(5);

    /**
     * 拿锁超时时间
     */
    private static final Duration TRY_LOCK_TIMEOUT = Duration.ofSeconds(15);

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    @Autowired
    public RedisTemplate redisTemplate;


    public boolean lock(String lockName) {
        long timeout = TRY_LOCK_TIMEOUT.toMillis() + System.currentTimeMillis();
        //拿不到锁 一直尝试拿到锁
        while (!redisTemplate.opsForValue().setIfAbsent(lockName,System.currentTimeMillis(),TIMEOUT)) {
            if (System.currentTimeMillis() > timeout) {
                //超时处理
                LOGGER.error("获取锁超时 : " + lockName);
                return false;
            }
        }
        //规定时间内拿到锁
        return true;
    }


    public void unlock(String lockName) {
        redisTemplate.opsForValue().getOperations().delete(lockName);
    }

}
