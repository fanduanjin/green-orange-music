package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

/**
 * @author fanduanjin
 * @Description 获取歌单信息
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public abstract class AbstractSongOrderInfoRequest<T> extends AbstractDebugRequest<T> {

    private static final String GROUP="req1";
    private static final String MODULE="music.srfDissInfo.aiDissInfo";
    private static final String METHOD="uniform_get_Dissinfo";
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    protected final ThreadLocal<Long> localSongOrderId=new ThreadLocal<>();
    public AbstractSongOrderInfoRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getSongOrderInfo(long songOrderId){
        localSongOrderId.set(songOrderId);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SongOrderInfoRequestParam  songOrderInfoRequestParam=new SongOrderInfoRequestParam();
        songOrderInfoRequestParam.setSongOrderId(localSongOrderId.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,songOrderInfoRequestParam);
    }


    @Override
    protected void finallyProcessor() {
        localSongOrderId.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    //{"disstid":1886197606,"userinfo":1,"tag":1,"orderlist":1,"song_begin":0,"song_num":10,"onlysonglist":0,"enc_host_uin":""}
    @Data
    private class SongOrderInfoRequestParam{
        @JsonProperty("disstid")
        private Long songOrderId;
        @JsonProperty("song_begin")
        private int begin=0;
        @JsonProperty("song_num")
        private int num=1;

        /**
         * 显示收藏数量
         */
        @JsonProperty("orderlist")
        private int orderNum=1;
    }
}
