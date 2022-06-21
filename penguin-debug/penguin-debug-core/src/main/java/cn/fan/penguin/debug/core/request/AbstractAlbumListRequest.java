package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public abstract class AbstractAlbumListRequest<T> extends AbstractDebugRequest<T> {
    private static final String GROUP="album_list";
    private static final String MODULE="music.musichallAlbum.AlbumListServer";
    private static final String METHOD="GetAlbumList";
    private static final int PAGE_SIZE=20;
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    private final ThreadLocal<String> localSingerMid=new ThreadLocal<>();
    private final ThreadLocal<Integer> localPageIndex=new ThreadLocal<>();
    public AbstractAlbumListRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getAlbumList(String singerMid,int pageIndex){
        pageIndex=pageIndex<0?0:pageIndex;
        localSingerMid.set(singerMid);
        localPageIndex.set(pageIndex);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        AlbumListRequestParam albumListRequestParam=new AlbumListRequestParam();
        albumListRequestParam.setMid(localSingerMid.get());
        int pageIndex=localPageIndex.get();
        int begin=pageIndex<2?0:(pageIndex-1)*PAGE_SIZE;
        albumListRequestParam.setBegin(begin);
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,albumListRequestParam);
    }

    @Override
    protected void finallyProcessor() {
        localSingerMid.remove();
        localPageIndex.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class AlbumListRequestParam{
        @JsonProperty("singerMid")
        private String mid;
        private int begin;
        private int num=PAGE_SIZE;
    }

}
