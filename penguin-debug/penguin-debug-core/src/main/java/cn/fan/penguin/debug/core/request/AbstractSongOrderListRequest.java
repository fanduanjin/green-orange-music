package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public abstract class AbstractSongOrderListRequest<T> extends AbstractDebugRequest<T> {

    private static final String GROUP = "req1";
    private static final String MODULE = "music.srfDissInfo.aiDissInfo";
    private static final String METHOD = "uniform_get_Dissinfo";
    private static final int PAGE_SIZE=30;
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    protected final ThreadLocal<Long> localSongOrderId = new ThreadLocal<>();
    protected final ThreadLocal<Integer> localPageIndex = new ThreadLocal<>();


    public AbstractSongOrderListRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator = penguinRequestParameterCreator;
    }

    public DebugResult<T> getSongOrderList(long songOrderId, int pageIndex) {
        localSongOrderId.set(songOrderId);
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        localPageIndex.set(pageIndex);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SongOrderListRequestParam songOrderListRequestParam = new SongOrderListRequestParam();
        songOrderListRequestParam.setSongOrderId(localSongOrderId.get());
        int pageIndex=localPageIndex.get();
        songOrderListRequestParam.setBegin(pageIndex<2?0:(pageIndex-1)*PAGE_SIZE);

        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP, MODULE, METHOD, songOrderListRequestParam);
    }


    @Override
    protected void finallyProcessor() {
        localSongOrderId.remove();
        localPageIndex.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    //{"disstid":1886197606,"userinfo":1,"tag":1,"orderlist":1,"song_begin":0,"song_num":10,"onlysonglist":0,
    // "enc_host_uin":""}
    @Data
    private class SongOrderListRequestParam {
        @JsonProperty("disstid")
        private Long songOrderId;
        @JsonProperty("song_begin")
        private int begin;
        @JsonProperty("song_num")
        private int num=PAGE_SIZE;
        @JsonProperty("onlysonglist")
        private int onlySongList = 1;

    }
}
