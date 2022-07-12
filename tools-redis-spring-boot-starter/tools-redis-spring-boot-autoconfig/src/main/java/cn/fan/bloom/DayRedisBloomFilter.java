package cn.fan.bloom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author fanduanjin
 * @Description 失效日期24小时的布隆过滤器
 * @Date 2022/7/12
 * @Created by fanduanjin
 */
@Component
public class DayRedisBloomFilter extends AbstractRedisBloomFilter {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");

    @Autowired
    DayRedisBloomFilter(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
    }


    @Override
    protected void setExpire(String key) {
        LocalTime localTime = LocalTime.now();
        //距离第二天隔离多少小时
        int hourOffset = 24 - localTime.getHour();
        //距离下个小时还有多少分钟+5
        int minuteOffset = 60 - localTime.getMinute() + 5;
        //将下个失效小时转化为分钟 做累加 作为key失效日期
        minuteOffset += hourOffset * 60;
        stringRedisTemplate.expire(key, Long.valueOf(minuteOffset), TimeUnit.MINUTES);
    }

    @Override
    protected String renameKey(String key) {
        LocalDate localDate = LocalDate.now();
        key += dateTimeFormatter.format(localDate);
        return key;
    }
}
