package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class SongTotalRequest extends AbstractDebugRequest<Integer> {

    private static final String GROUP="song_total";
    private static final String MODULE="musichall.song_list_server";
    private static final String METHOD="GetSingerSongList";

    private final ThreadLocal<String> localSingerMid=new ThreadLocal<>();

    public DebugResult<Integer> getSongTotal(String singerMid){
        localSingerMid.set(singerMid);
        return request();
    }

    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    public SongTotalRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SongTotalRequestParam songTotalRequestParam=new SongTotalRequestParam();
        songTotalRequestParam.setSingerMid(localSingerMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,songTotalRequestParam);
    }

    @Override
    protected Integer convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode)  {
        return dataNode.get("totalNum").asInt();
    }

    @Override
    protected void finallyProcessor() {
        localSingerMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class SongTotalRequestParam{
        private int order=1;
        private String singerMid;
        private int begin=0;
        private int num=1;
    }

}
