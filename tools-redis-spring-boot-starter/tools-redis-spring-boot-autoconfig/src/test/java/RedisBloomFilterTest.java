import com.sun.tracing.dtrace.ArgsAttributes;
import org.junit.Test;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
public class RedisBloomFilterTest {
    DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yy-MM-dd HH");

    @Test
    public void ttt(){
        LocalDateTime localDateTime=LocalDateTime.now();
        System.out.println(dateTimeFormatter.format(localDateTime));
    }
}
