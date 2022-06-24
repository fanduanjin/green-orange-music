package cn.fan;

import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.http.Promise;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.SingerTotalRequest;
import cn.fan.penguin.debug.core.request.SongTotalRequest;
import cn.fan.penguin.debug.request.SingerListRequestImpl;
import cn.fan.penguin.debug.request.SongListRequestImpl;
import cn.hutool.core.util.PageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/22
 * @Created by fanduanjin
 */

@Component
public class RedisTest implements InitializingBean {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public static int num=0;
    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.opsForValue().set("test","fafasdfa");
        if(num==0) {
            return;
        }
        PenguinRequestParameterCreator penguinRequestParameterCreator=new PenguinRequestParameterCreator(objectMapper);
        SingerTotalRequest singerTotalRequest=new SingerTotalRequest(penguinRequestParameterCreator);
        SongListRequestImpl songListRequest=new SongListRequestImpl(penguinRequestParameterCreator);
        SingerListRequestImpl singerListRequest=new SingerListRequestImpl(penguinRequestParameterCreator);
        SongTotalRequest songTotalRequest=new SongTotalRequest(penguinRequestParameterCreator);
        DebugResult<Integer> singerTotalResult=singerTotalRequest.getSingerTotal();
        if(!singerTotalResult.isSuccess()) {
            System.out.println(singerTotalResult.getMessage());
            return;
        }
        int singerTotal= singerTotalResult.getData();
        long maxId=0;
        int singerTotalPage= PageUtil.totalPage(singerTotal, SingerListRequestImpl.SIN_OFFSET);
        Promise<List<Singer>> singerListPromise=new Promise<>();
        singerListPromise.success(singers -> {
            for(Singer singer:singers){
                DebugResult<Integer> songTotalResult= songTotalRequest.getSongTotal(singer.getMid());
                if(!songTotalResult.isSuccess()){
                    System.out.println(songTotalResult.getMessage());
                    continue;
                }
                num+=songTotalResult.getData();
            }
        }).failed(message -> System.out.println(message));
        for(int i=0;i<singerTotalPage;i++){
            DebugResult<List<Singer>> singerListResult= singerListRequest.getSingerList(i);
            singerListPromise.when(singerListResult);
            System.out.println(i);
        }
        System.out.println("总歌曲数量"+num);
    }
}
