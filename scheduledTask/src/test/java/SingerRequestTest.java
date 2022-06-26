import cn.fan.ServerScheduledTask;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.http.Promise;
import cn.fan.penguin.debug.core.request.SingerTotalRequest;
import cn.fan.penguin.debug.request.SingerInfoRequestImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/26
 * @Created by fanduanjin
 */
@RunWith(SpringRunner.class)
@SpringBootTest( classes = ServerScheduledTask.class)
public class SingerRequestTest {

    @Autowired
    SingerTotalRequest singerTotalRequest;

    @Autowired
    SingerInfoRequestImpl singerInfoRequest;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void singerTotalRequest() throws JsonProcessingException {
        DebugResult<Integer> singerTotalResult= singerTotalRequest.getSingerTotal();
        Promise<Integer> promise=new Promise();
        promise.success(total->{
            System.out.println("total : "+total);
        }).failed(message -> System.out.println(message));
        promise.when(singerTotalResult);
    }
}
