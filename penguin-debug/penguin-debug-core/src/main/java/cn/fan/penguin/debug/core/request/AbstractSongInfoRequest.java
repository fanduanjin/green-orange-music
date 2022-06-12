package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.win32.Winspool;
import lombok.Data;

import java.io.IOException;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public abstract class AbstractSongInfoRequest<T> extends AbstractDebugRequest<T> {

    private static final String GROUP="song_info";
    private static final String MODULE="music.pf_song_detail_svr";
    private static final String METHOD="get_song_detail_yqq";
    private final ThreadLocal<String> localSongMid=new ThreadLocal<>();


    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    public AbstractSongInfoRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getSongInfo(String songMid){
        localSongMid.set(songMid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SongInfoRequestParam songInfoRequestParam=new SongInfoRequestParam(localSongMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,songInfoRequestParam);
    }



    @Override
    protected void finallyProcessor() {
        localSongMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class SongInfoRequestParam{
        SongInfoRequestParam(String songMid){
            this.songMid=songMid;
        }
        @JsonProperty("song_mid")
        private String songMid;
    }
}
