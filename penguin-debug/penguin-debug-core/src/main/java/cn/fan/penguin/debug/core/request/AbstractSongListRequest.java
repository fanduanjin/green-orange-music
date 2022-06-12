package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
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
public abstract class AbstractSongListRequest<T> extends AbstractDebugRequest<T> {
    private static final String GROUP="song_list";
    private static final String MODULE="musichall.song_list_server";
    private static final String METHOD="GetSingerSongList";
    public static final Integer DEFAULT_PAGE_SIZE=50;

    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    private final ThreadLocal<String> localSingerMid=new ThreadLocal<>();
    private final ThreadLocal<Integer> localPageIndex=new ThreadLocal<>();
    private final ThreadLocal<Integer> localPageSize=new ThreadLocal<>();

    public AbstractSongListRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getSongList(String singerMid,Integer pageIndex,Integer pageSize){
        localSingerMid.set(singerMid);
        if(pageIndex==null){
            pageIndex=1;
        }
        if(pageSize==null){
            pageSize=DEFAULT_PAGE_SIZE;
        }
        localPageIndex.set(pageIndex);
        localPageSize.set(pageSize);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SongListRequestParam songListRequestParam=new SongListRequestParam();
        songListRequestParam.setNum(localPageSize.get());
        songListRequestParam.setSingerMid(localSingerMid.get());
        int pageIndex=localPageIndex.get();
        if(pageIndex>1){
            songListRequestParam.setBegin((pageIndex-1)* songListRequestParam.getNum());
        }else {
            songListRequestParam.setBegin(0);
        }
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,songListRequestParam);
    }


    @Override
    protected void finallyProcessor() {
        localPageSize.remove();
        localPageIndex.remove();
        localSingerMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class SongListRequestParam{
        private int order=1;
        private String singerMid;
        private Integer begin;
        private Integer num;
    }
}
