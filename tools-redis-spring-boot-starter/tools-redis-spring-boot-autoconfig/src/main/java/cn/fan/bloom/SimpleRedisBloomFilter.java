package cn.fan.bloom;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author fanduanjin
 * @Description 每小时更换一次key
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
@Component
public class SimpleRedisBloomFilter extends AbstractRedisBloomFilter {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH");

    @Autowired
    SimpleRedisBloomFilter(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }

    @Override
    protected void setExpire(String key) {
        LocalTime localTime = LocalTime.now();
        //计算下个小时05分时间 作为key失效时间
        int offset = 60 - localTime.getMinute() + 5;
        stringRedisTemplate.expire(key,offset,TimeUnit.MINUTES);
    }

    @Override
    protected String renameKey(String key) {
        LocalDateTime localDateTime = LocalDateTime.now();
        key += dateTimeFormatter.format(localDateTime);
        return key;
    }
}
