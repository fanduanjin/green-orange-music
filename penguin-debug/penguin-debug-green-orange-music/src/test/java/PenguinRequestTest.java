import cn.fan.model.Album;
import cn.fan.model.Mv;
import cn.fan.model.Singer;
import cn.fan.model.Song;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.http.ResultHandler;
import cn.fan.penguin.debug.core.model.MediaUrlInfo;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.*;
import cn.fan.penguin.debug.request.*;
import cn.hutool.core.util.PageUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/8
 * @Created by fanduanjin
 */
public class PenguinRequestTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    PenguinRequestParameterCreator penguinRequestParameterCreator = new PenguinRequestParameterCreator(objectMapper);

    static {
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void singerInfoRequest() throws JsonProcessingException {
        SingerInfoRequestImpl singerInfoRequest = new SingerInfoRequestImpl(penguinRequestParameterCreator);
        //男歌手 周杰伦0025NhlN2yWrP4
        //组合 南征北战  003ZQQb64D5317
        DebugResult<List<Singer>> result = singerInfoRequest.getSingerInfoResult("0025NhlN2yWrP4", "003ZQQb64D5317");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }

    }

    @Test
    public void lyricRequestTest() {
        LyricRequest lyricRequest = new LyricRequest(objectMapper);
        //歌曲 沦陷
        DebugResult<String> result = lyricRequest.getSongLyric("001BTwv31D0hbb");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + result.getData());
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void SongMediaUrlRequestTest() throws JsonProcessingException {
        SongMediaUrlRequest lyricRequest = new SongMediaUrlRequest(penguinRequestParameterCreator);
        //歌曲 沦陷001BTwv31D0hbb
        //歌曲 爱，存在 vip 000hsEco0QN1do
        DebugResult<List<MediaUrlInfo>> result = lyricRequest.getSongMediaUrl("001BTwv31D0hbb",
                "002BL4nH09vMJs");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void SingerTotalRequestTest() throws JsonProcessingException {
        long start = System.currentTimeMillis();
        SingerTotalRequest singerTotalRequest = new SingerTotalRequest(penguinRequestParameterCreator);
        DebugResult result = singerTotalRequest.getSingerTotal();
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
        System.out.println("耗时 : " + (System.currentTimeMillis() - start));
    }

    @Test
    public void SingerListRequestTest() throws JsonProcessingException {
        SingerTotalRequest singerTotalRequest = new SingerTotalRequest(penguinRequestParameterCreator);
        DebugResult<Integer> cloudTotalResult = singerTotalRequest.getSingerTotal();
        if (cloudTotalResult.isSuccess()) {
            int cloudTotal = cloudTotalResult.getData();
            int debugTotal = 0;
            SingerListRequestImpl singerListRequest = new SingerListRequestImpl(penguinRequestParameterCreator);
            int pageTotal = PageUtil.totalPage(cloudTotal, AbstractSingerListRequest.SIN_OFFSET);
            long start = System.currentTimeMillis();
            for (int pageIndex = 1; pageIndex <= pageTotal; pageIndex++) {
                DebugResult<List<Singer>> singerListResult = singerListRequest.getSingerList(pageIndex);
                if (singerListResult.isSuccess()) {
                    debugTotal += singerListResult.getData().size();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("应爬取数量 : " + cloudTotal + "  实际爬取数量 : " + debugTotal);
            long com = end - start;
            System.out.println("总消耗时间 : " + com + "  平均消耗时间 : " + com / pageTotal);
        } else {
            System.out.println("获取歌手总数失败 : " + cloudTotalResult.getMessage());
        }

    }

    @Test
    public void songTotalRequestTest() throws JsonProcessingException {
        SongTotalRequest songTotalRequest = new SongTotalRequest(penguinRequestParameterCreator);
        //周杰伦
        DebugResult result = songTotalRequest.getSongTotal("0025NhlN2yWrP4");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void songListRequestTest() throws JsonProcessingException {
        //周杰伦
        String singerMid = "0025NhlN2yWrP4";
        SongTotalRequest songTotalRequest = new SongTotalRequest(penguinRequestParameterCreator);
        DebugResult<Integer> result = songTotalRequest.getSongTotal(singerMid);
        if (result.isSuccess()) {
            int cloudSongTotal = result.getData();
            int songTotalNum = 0;
            int pageSize = AbstractSongListRequest.DEFAULT_PAGE_SIZE;
            int totalPage = PageUtil.totalPage(cloudSongTotal, pageSize);
            for (int i = 1; i <= totalPage; i++) {
                SongListRequestImpl songListRequest = new SongListRequestImpl(penguinRequestParameterCreator);
                DebugResult<List<Song>> songListResult = songListRequest.getSongList(singerMid, i, pageSize);
                if (songListResult.isSuccess()) {
                    songTotalNum += songListResult.getData().size();
                }
            }
            System.out.println("应爬取数量 : " + cloudSongTotal + "  实际爬取数量" + songTotalNum);
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void songInfoRequestTest() throws JsonProcessingException {
        long start = System.currentTimeMillis();
        SongInfoRequestImpl songInfoRequest = new SongInfoRequestImpl(penguinRequestParameterCreator);
        DebugResult result = songInfoRequest.getSongInfo("001PLl3C4gPSCI");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
        System.out.println(" 耗时 " + (System.currentTimeMillis() - start));

    }

    @Test
    public void albumInfoRequestTest() throws JsonProcessingException {
        AlbumInfoRequestImpl albumInfoRequest = new AlbumInfoRequestImpl(penguinRequestParameterCreator);
        //三国恋2022
        DebugResult<Album> result = albumInfoRequest.getAlbumInfo("002Tsgf504IQTv");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void mvInfoRequestTest() throws JsonProcessingException {
        MvInfoRequestImpl mvInfoRequest = new MvInfoRequestImpl(penguinRequestParameterCreator);
        //三国恋2022
        DebugResult<List<Mv>> result = mvInfoRequest.getMvInfo("i0013atzhyk", "r00904ing4e");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }


    @Test
    public void mvUrlRequestTest() throws JsonProcessingException {
        MvUrlRequest mvUrlRequest = new MvUrlRequest(penguinRequestParameterCreator);
        DebugResult result = mvUrlRequest.getMvUrls("i0013atzhyk", "r00904ing4e");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void albumListRequestTest() throws JsonProcessingException {
        AlbumListRequestImpl albumListRequest = new AlbumListRequestImpl(penguinRequestParameterCreator);
        DebugResult result = albumListRequest.getAlbumList("0025NhlN2yWrP4", 2);
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void mvListRequestTest() throws JsonProcessingException {
        MvListRequestImpl mvListRequest = new MvListRequestImpl(penguinRequestParameterCreator);
        DebugResult result = mvListRequest.getMvList("0025NhlN2yWrP4", 2);
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void albumTotalRequestTest() throws JsonProcessingException {
        AlbumTotalRequest albumTotalRequest = new AlbumTotalRequest(penguinRequestParameterCreator);
        DebugResult result = albumTotalRequest.getTotal("003Nz2So3XXYek");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void mvTotalRequestTest() throws JsonProcessingException {
        MvTotalRequest mvTotalRequest = new MvTotalRequest(penguinRequestParameterCreator);
        DebugResult result = mvTotalRequest.getTotal("003Nz2So3XXYek");
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
    }

    @Test
    public void songOrderInfoRequestTest() throws JsonProcessingException {
        SongOrderInfoRequestImpl songOrderInfoRequest = new SongOrderInfoRequestImpl(penguinRequestParameterCreator);
        long start = System.currentTimeMillis();

        DebugResult result = songOrderInfoRequest.getSongOrderInfo(1886197606L);
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
        System.out.println(System.currentTimeMillis() - start);

    }

    @Test
    public void songOrderListRequestTest() throws JsonProcessingException {
        //7994616672

        SongOrderListRequestImpl songOrderListRequest = new SongOrderListRequestImpl(penguinRequestParameterCreator);
        long start = System.currentTimeMillis();

        DebugResult result =
                songOrderListRequest.getSongOrderList(7994616672L, 2);
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }
        System.out.println(System.currentTimeMillis() - start);
    }


    @Test
    public void test2() throws IOException {
        long start = System.currentTimeMillis();
        Document document = Jsoup.connect("https://y.qq.com/n/ryqq/category").get();
        Element e = document.selectFirst("body>script");
        String json = e.html();
        json = json.substring(json.indexOf('{'));

        System.out.println(json);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void test32() throws IOException {
        CategoryInfoRequestImpl categoryInfoRequest = new CategoryInfoRequestImpl(objectMapper);
        long start = System.currentTimeMillis();
        DebugResult result= categoryInfoRequest.getCategoryInfo();
        if (result.isSuccess()) {
            System.out.println("爬取成功 : " + objectMapper.writeValueAsString(result.getData()));
        } else {
            System.out.println("爬取失败 : " + result.getMessage());
        }

        System.out.println(System.currentTimeMillis() - start);
    }

}
