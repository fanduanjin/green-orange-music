package cn.fan.mq;

import cn.fan.bloom.DayRedisBloomFilter;
import cn.fan.bloom.SimpleRedisBloomFilter;
import cn.fan.model.Song;
import cn.fan.model.constrans.QueuedName;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.request.SongInfoRequestImpl;
import cn.fan.service.SongServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author fanduanjin
 * @Description 同步歌曲信息
 * @Date 2022/7/10
 * @Created by fanduanjin
 */

@Component
@RabbitListener(queuesToDeclare = @Queue(QueuedName.SYNC_SONG_INFO), concurrency = "8")
public class SyncSongInfoConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncSongInfoConsumer.class);

    @Autowired
    SongInfoRequestImpl songInfoRequest;

    @Autowired
    SongServiceImpl songService;

    @Autowired
    DayRedisBloomFilter redisBloomFilter;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @RabbitHandler
    void syncSongInfo(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(songs.size());
        for (Song song : songs) {
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    syncSongInfo(song);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
        LOGGER.info(QueuedName.SYNC_SONG_INFO + Thread.currentThread().getName() + songs.size());
    }

    void syncSongInfo(Song song) {
        if (redisBloomFilter.exists(QueuedName.SYNC_SONG_INFO, song.getId())) {
            //近期同步过 或者 爬虫失败
            return;
        }
        DebugResult<Song> songInfoResult = songInfoRequest.getSongInfo(song.getMid());
        if (!songInfoResult.isSuccess()) {
            return;
        }
        //调用api 爬取详情信息
        song = songInfoResult.getData();
        try {
            Song dbSong = songService.getSongById(song.getId());
            if (dbSong == null) {
                //不存在  插入
                songService.addSong(song);
            }
            //同步成功 维护bloom
            redisBloomFilter.add(QueuedName.SYNC_SONG_INFO, song.getId());
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

}
