package cn.fan.scheduled;

import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.http.Promise;
import cn.fan.penguin.debug.core.request.SingerTotalRequest;
import cn.fan.penguin.debug.request.SingerListRequestImpl;
import cn.hutool.core.util.PageUtil;
import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/26
 * @Created by fanduanjin
 */
@Component
public class DebugSingerListScheduled implements SchedulingConfigurer, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugSingerListScheduled.class);


    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private SingerTotalRequest singerTotalRequest;

    @Autowired
    private SingerListRequestImpl singerListRequest;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        LOGGER.info("提交定时任务");
        scheduledTaskRegistrar.addCronTask(this,"0 */1 * * * ?");
    }


    @Override
    public void run() {
        LOGGER.info("准备爬取歌手列表");
        DebugResult<Integer> singerTotalResult = singerTotalRequest.getSingerTotal();
        if (!singerTotalResult.isSuccess()) {
            LOGGER.warn(singerTotalResult.getMessage());
            return;
        }
        //获取全网歌手数量
        int singerTotal = singerTotalResult.getData();
        //根据分页大小计算总页数
        int totalPage = PageUtil.totalPage(singerTotal, SingerListRequestImpl.SIN_OFFSET);
        //创建真实爬取计数器
        AtomicInteger debugCount = new AtomicInteger();
        AtomicInteger debugPageCount=new AtomicInteger();
        //根据总页数创建countDownLatch
        CountDownLatch countDownLatch = new CountDownLatch(totalPage);
        //创建promise
        Promise<List<Singer>>  promise=new Promise<>();
        promise.success(singers ->{
            //累加计数器
            debugCount.addAndGet(singers.size());
            debugPageCount.incrementAndGet();
            //countDownLatch -1
            countDownLatch.countDown();
        }).fail(message->{
            LOGGER.warn(message.toString());
        });
        //根据分页大小创建future
        for (int i = 1; i <= totalPage; i++) {
            //创建线程异步处理
            int finalI = i;
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    DebugResult<List<Singer>> singerListResult=singerListRequest.getSingerList(finalI);
                    //将返回数据交给promise处理
                    promise.end(singerListResult);
                }
            },threadPoolTaskExecutor);
        }
        try {
            LOGGER.info("阻塞线程");
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
        LOGGER.info("歌手总数量 : "+singerTotal+"  歌手总页数"+totalPage);
        LOGGER.info("爬取总数量 : "+debugCount.get()+"  爬取总页数"+debugPageCount.get());
    }

}
