package cn.fan.mq;

import cn.fan.bloom.HalfRedisBloomFilter;
import cn.fan.bloom.SimpleRedisBloomFilter;
import cn.fan.model.Singer;
import cn.fan.model.Song;
import cn.fan.model.constrans.QueuedName;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.http.Promise;
import cn.fan.penguin.debug.core.request.SongTotalRequest;
import cn.fan.penguin.debug.request.SongListRequestImpl;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fanduanjin
 * @Description 同步歌曲列表
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(QueuedName.SYNC_SONG_LIST), concurrency = "4")
public class SyncSongListConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncSongListConsumer.class);
    private static final int SONG_LIST_SIZE = 30;

    @Autowired
    HalfRedisBloomFilter redisBloomFilter;

    @Autowired
    SongListRequestImpl songListRequest;

    @Autowired
    SongTotalRequest songTotalRequest;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void syncSongList(Singer singer) {
        DebugResult<Integer> songTotalResult = songTotalRequest.getSongTotal(singer.getMid());
        if (!songTotalResult.isSuccess() || redisBloomFilter.exists(QueuedName.SYNC_SONG_LIST, singer.getId())) {
            //近期同步过 或者获取total失败
            return;
        }
        long start = System.currentTimeMillis();
        int songTotal = songTotalResult.getData();
        int pageTotal = PageUtil.totalPage(songTotal, SONG_LIST_SIZE);
        AtomicInteger debugTotal = new AtomicInteger();
        Promise<List<Song>> songListPromise = new Promise<>();
        songListPromise.success(songs -> {
            debugTotal.addAndGet(songs.size());
            rabbitTemplate.convertAndSend(QueuedName.SYNC_SONG_INFO, songs);
        }).fail(message -> {
            LOGGER.error(message.toString());
        });
        CountDownLatch countDownLatch = new CountDownLatch(pageTotal);
        for (int i = 0; i <= pageTotal; i++) {
            int finalI = i;
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    DebugResult<List<Song>> songListResult = songListRequest.getSongList(singer.getMid(), finalI,
                            SONG_LIST_SIZE);
                    songListPromise.end(songListResult);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }

        redisBloomFilter.add(QueuedName.SYNC_SONG_LIST, singer.getId());
        LOGGER.info(singer.getName() + " - 总歌曲数量 : " + songTotal + "   爬取歌曲总数量 : " + debugTotal.get() + "  耗时 : " + (System.currentTimeMillis() - start));
    }
}
