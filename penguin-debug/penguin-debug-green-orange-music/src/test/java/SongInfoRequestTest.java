import cn.fan.model.Singer;
import cn.fan.model.Song;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSongListRequest;
import cn.fan.penguin.debug.core.request.SongTotalRequest;
import cn.fan.penguin.debug.request.SingerListRequestImpl;
import cn.fan.penguin.debug.request.SongInfoRequestImpl;
import cn.fan.penguin.debug.request.SongListRequestImpl;
import cn.hutool.core.util.PageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class SongInfoRequestTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    PenguinRequestParameterCreator penguinRequestParameterCreator = new PenguinRequestParameterCreator(objectMapper);

    @Test
    public void exampleTest() {
        //周杰伦
        String singerMid = "0025NhlN2yWrP4";
        SongTotalRequest songTotalRequest = new SongTotalRequest(penguinRequestParameterCreator);
        SongInfoRequestImpl songInfoRequest = new SongInfoRequestImpl(penguinRequestParameterCreator);
        DebugResult<Integer> result = songTotalRequest.getSongTotal(singerMid);
        if (result.isSuccess()) {
            int cloudSongTotal = result.getData();
            int songTotalNum = 0;
            int pageSize = AbstractSongListRequest.DEFAULT_PAGE_SIZE;
            int totalPage = PageUtil.totalPage(cloudSongTotal, pageSize);
            int songInfoNum = 0;
            for (int i = 1; i <= totalPage; i++) {
                SongListRequestImpl songListRequest = new SongListRequestImpl(penguinRequestParameterCreator);
                DebugResult<List<Song>> songListResult = songListRequest.getSongList(singerMid, i, pageSize);
                if (songListResult.isSuccess()) {
                    songTotalNum += songListResult.getData().size();
                    for (Song song : songListResult.getData()) {
                        DebugResult songInfoResult = songInfoRequest.getSongInfo(song.getMid());
                        if (songInfoResult.isSuccess()) {
                            songInfoNum++;
                        }
                    }
                }
            }
            System.out.println("应爬取数量 : " + cloudSongTotal + "  实际爬取数量" + songTotalNum);
            System.out.println("songInfo 爬取成功总数 : " + songInfoNum);
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void testAll() {
        SingerListRequestImpl singerListRequest = new SingerListRequestImpl(penguinRequestParameterCreator);
        SongListRequestImpl songListRequest = new SongListRequestImpl(penguinRequestParameterCreator);
        SongInfoRequestImpl songInfoRequest = new SongInfoRequestImpl(penguinRequestParameterCreator);
        SongTotalRequest songTotalRequest = new SongTotalRequest(penguinRequestParameterCreator);
        int singerListPage = 4;
        CountDownLatch countDownLatch = new CountDownLatch(4 * 80);
        for (int pageIndex = 1; pageIndex <= singerListPage; pageIndex++) {
            DebugResult<List<Singer>> singerListResult = singerListRequest.getSingerList(pageIndex);
            if (!singerListResult.isSuccess()) {
                System.out.println("singerListResult failed");
                continue;
            }
            for (Singer singer : singerListResult.getData()) {
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        DebugResult<Integer> songTotalResult = songTotalRequest.getSongTotal(singer.getMid());
                        if (!songTotalResult.isSuccess()) {
                            countDownLatch.countDown();
                            return;
                        }
                        int songTotal = songTotalResult.getData();
                        //计算总歌曲数量
                        ShareConst.totalNum(songTotal);
                        int pageTotal = PageUtil.totalPage(songTotal, SongListRequestImpl.DEFAULT_PAGE_SIZE);
                        for (int i = 1; i <= pageTotal; i++) {
                            DebugResult<List<Song>> songListResult = songListRequest.getSongList(singer.getMid(), i,
                                    SongListRequestImpl.DEFAULT_PAGE_SIZE);
                            if(!songListResult.isSuccess()){
                                continue;
                            }
                            for(Song song:songListResult.getData()){
                                DebugResult<Song> songInfoResult= songInfoRequest.getSongInfo(song.getMid());
                                if(songInfoResult.isSuccess()){
                                    //计算成功爬取数量
                                    ShareConst.incNum();
                                }
                            }
                        }
                        countDownLatch.countDown();
                    }
                }, ShareConst.executor);
            }
        }
        try {
            countDownLatch.await(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("应该爬取数量 : "+ShareConst.getTotalNum()+"  爬取成功数量 : "+ShareConst.getIncNum());
    }

}
