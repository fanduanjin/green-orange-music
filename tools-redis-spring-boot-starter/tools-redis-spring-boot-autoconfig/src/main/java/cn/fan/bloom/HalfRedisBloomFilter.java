package cn.fan.bloom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/12
 * @Created by fanduanjin
 */
@Component
public class HalfRedisBloomFilter extends AbstractRedisBloomFilter {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");

    @Autowired
    HalfRedisBloomFilter(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }

    @Override
    protected void setExpire(String key) {
        LocalTime localTime = LocalTime.now();
        int hourOffset = Math.abs(12 - localTime.getHour());
        int minuteOffset = 60 - localTime.getMinute() + 5;
        minuteOffset += hourOffset * 60;
        stringRedisTemplate.expire(key, Long.valueOf(minuteOffset), TimeUnit.MINUTES);
    }

    @Override
    protected String renameKey(String key) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.getHour() > 12) {
            //PM 下午  key2022-02-02:PM
            key += dateTimeFormatter.format(localDateTime) + "=PM";
        } else {
            //AM 上午
            key += dateTimeFormatter.format(localDateTime) + "=AM";
        }
        return key;
    }
}
