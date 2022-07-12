import cn.fan.ServerScheduledTask;
import cn.fan.api.fegin.ICategoryServer;
import cn.fan.bloom.SimpleRedisBloomFilter;
import cn.fan.lock.RedisLock;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.http.Promise;
import cn.fan.penguin.debug.core.request.SingerTotalRequest;
import cn.fan.penguin.debug.request.CategoryInfoRequestImpl;
import cn.fan.penguin.debug.request.SingerInfoRequestImpl;
import cn.hutool.core.util.HashUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/26
 * @Created by fanduanjin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerScheduledTask.class)
public class SingerRequestTest {

    @Autowired
    SingerTotalRequest singerTotalRequest;

    @Autowired
    SingerInfoRequestImpl singerInfoRequest;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CategoryInfoRequestImpl categoryInfoRequest;

    @Autowired
    ICategoryServer categoryServer;

    @Autowired
    RedisLock redisLock;

    @Autowired
    SimpleRedisBloomFilter simpleRedisBloomFilter;

    @Test
    public void singerTotalRequest() throws JsonProcessingException {
        DebugResult<Integer> singerTotalResult = singerTotalRequest.getSingerTotal();
        Promise<Integer> promise = new Promise();
        promise.success(total -> {
            System.out.println("total : " + total);
        }).fail(message -> System.out.println(message));
        promise.end(singerTotalResult);
    }

    @Test
    public void categoryRequestTest() {
        String tmp = SecureUtil.md5("fanduanjin");
        System.out.println();
    }


    @Test
    public void testRedisBloom(){
        Arrays.asList("tests");
        System.out.println("cccc");
        simpleRedisBloomFilter.add("test",13);
        System.out.println(simpleRedisBloomFilter.exists("test",13));
        System.out.println(simpleRedisBloomFilter.exists("test",143));

        System.out.println("redis lock test");
        System.out.println(redisLock.lock("testlocal"));
        System.out.println(redisLock.lock("testlocal"));


    }

}
