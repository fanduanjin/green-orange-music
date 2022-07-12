package cn.fan.mq;

import cn.fan.api.service.ISingerService;
import cn.fan.bloom.HalfRedisBloomFilter;
import cn.fan.bloom.SimpleRedisBloomFilter;
import cn.fan.dao.SingerRepository;
import cn.fan.model.Singer;
import cn.fan.model.constrans.QueuedName;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.request.SingerInfoRequestImpl;
import cn.fan.service.SingerServiceImpl;
import cn.hutool.core.collection.CollectionUtil;
import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/27
 * @Created by fanduanjin
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(QueuedName.SYNC_SINGER_INFO), concurrency = "4")
public class SyncSingerInfoConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncSingerInfoConsumer.class);

    @Autowired
    SingerInfoRequestImpl singerInfoRequest;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    ISingerService singerService;

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    HalfRedisBloomFilter redisBloomFilter;

    @RabbitHandler()
    public void syncSingerInfo(List<Singer> singerList) {
        if (CollectionUtil.isEmpty(singerList)) {
            return;
        }
        long start = System.currentTimeMillis();
        //拿到所有mid
        String[] mids = new String[singerList.size()];
        for (int i = 0, l = singerList.size(); i < l; i++) {
            mids[i] = singerList.get(i).getMid();
        }
        //调用api 查询详情信息
        DebugResult<List<Singer>> singerListResult = singerInfoRequest.getSingerInfoResult(mids);
        if (!singerListResult.isSuccess()) {
            LOGGER.warn(singerListResult.getMessage());
            return;
        }
        for (Singer singer : singerListResult.getData()) {
            doHandlerSinger(singer);
        }
        LOGGER.info("同步歌手列表 :当前页大小 : " + singerList.size() + "  同步时间 : " + (System.currentTimeMillis() - start));
    }

    void doHandlerSinger(Singer singer) {
        if (redisBloomFilter.exists(QueuedName.SYNC_SINGER_INFO, singer.getId())) {
            //不容过滤器如果有值 说明最近通不过 跳过
            return;
        }
        try {
            Singer dbSinger = singerService.getSingerById(singer.getId());
            if (dbSinger == null) {
                //不存在就插入到数据库
                singerService.addSinger(singer);
            }

            //爬取成功后 继续后续爬取 专辑 歌曲 mv
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    rabbitTemplate.convertAndSend(QueuedName.SYNC_SONG_LIST, singer);
                }
            }, threadPoolTaskExecutor);
        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            redisBloomFilter.add(QueuedName.SYNC_SINGER_INFO, singer.getId());
        }
    }


}
