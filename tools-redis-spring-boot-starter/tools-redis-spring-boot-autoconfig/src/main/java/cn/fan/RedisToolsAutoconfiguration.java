package cn.fan;

import cn.fan.bloom.DayRedisBloomFilter;
import cn.fan.bloom.HalfRedisBloomFilter;
import cn.fan.bloom.SimpleRedisBloomFilter;
import cn.fan.lock.RedisLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
@Configuration
@Import({RedisLock.class, SimpleRedisBloomFilter.class, DayRedisBloomFilter.class, HalfRedisBloomFilter.class})
public class RedisToolsAutoconfiguration {
}
