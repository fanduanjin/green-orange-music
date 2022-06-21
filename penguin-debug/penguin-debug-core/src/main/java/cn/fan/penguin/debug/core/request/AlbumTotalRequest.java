package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public class AlbumTotalRequest extends AbstractDebugRequest<Integer> {
    private static final String GROUP="album_list";
    private static final String MODULE="music.musichallAlbum.AlbumListServer";
    private static final String METHOD="GetAlbumList";
    private static final int PAGE_SIZE=20;
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    private final ThreadLocal<String> localSingerMid=new ThreadLocal<>();
    public AlbumTotalRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<Integer> getTotal(String singerMid){
        localSingerMid.set(singerMid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
       AlbumTotalRequestParam albumTotalRequestParam=new AlbumTotalRequestParam();
        albumTotalRequestParam.setMid(localSingerMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,albumTotalRequestParam);
    }

    @Override
    protected Integer convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        return dataNode.get("total").asInt();
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
    private class AlbumTotalRequestParam{
        @JsonProperty("singerMid")
        private String mid;
        private int begin=0;
        private int num=0;
    }
}
