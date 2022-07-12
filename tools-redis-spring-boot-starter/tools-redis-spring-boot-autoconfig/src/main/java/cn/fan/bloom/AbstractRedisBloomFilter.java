package cn.fan.bloom;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/11
 * @Created by fanduanjin
 */
public abstract class AbstractRedisBloomFilter {


    private final RedisScript<Boolean> BLOOM_ADD_SCRIPTS = new DefaultRedisScript<>("return redis.call('bf" +
            ".add', KEYS[1], ARGV[1])", Boolean.class);
    private final RedisScript<Boolean> BLOOM_EXISTS_SCRIPTS = new DefaultRedisScript<>("return redis.call('bf" +
            ".exists', KEYS[1], ARGV[1])", Boolean.class);

    private static final TimedCache TIMED_CACHE = CacheUtil.newTimedCache(TimeUnit.MINUTES.toMillis(15));
    protected StringRedisTemplate stringRedisTemplate;

    AbstractRedisBloomFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void add(String key, long value) {
        add(key, String.valueOf(value));
    }

    public void add(String key, int value) {
        add(key, String.valueOf(value));
    }

    public void add(String key, String value) {
        key = renameKey(key);
        stringRedisTemplate.execute(BLOOM_ADD_SCRIPTS, Arrays.asList(key), value);
        validatorExpire(key);
    }

    public boolean exists(String key, long value) {
        return exists(key, String.valueOf(value));
    }

    public boolean exists(String key, int value) {
        return exists(key, String.valueOf(value));
    }

    public boolean exists(String key, String value) {
        key = renameKey(key);
        validatorExpire(key);
        return stringRedisTemplate.execute(BLOOM_EXISTS_SCRIPTS, Arrays.asList(key),value);
    }

    private void validatorExpire(String key) {
        if (TIMED_CACHE.get(key) != null) {
            //不等于null 说明最近验证过了
            return;
        }
        long expire = stringRedisTemplate.getExpire(key);
        if (expire > 0) {
            //大于0 说明 之前设置过过期时间
            return;
        }
        //调用钩子方法 设置超时时间
        setExpire(key);
        TIMED_CACHE.put(key, System.currentTimeMillis());
    }

    /**
     * 设置超时时间
     */
    protected abstract void setExpire(String key);

    /**
     * 重命名这个key 可以实现每隔一段时间洛到不同key上 queue-2012-12-12
     *
     * @param key
     */
    protected abstract String renameKey(String key);


}
