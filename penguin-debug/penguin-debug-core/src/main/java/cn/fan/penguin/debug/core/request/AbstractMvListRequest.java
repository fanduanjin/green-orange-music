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
public abstract class AbstractMvListRequest<T> extends AbstractDebugRequest<T> {

    private static final String GROUP="mv_list";
    private static final String MODULE="MvService.MvInfoProServer";
    private static final String METHOD="GetSingerMvList";
    private static final int PAGE_SIZE=50;

    private final ThreadLocal<String> localSingerMid=new ThreadLocal<>();
    private final ThreadLocal<Integer> localPageIndex=new ThreadLocal<>();
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    public AbstractMvListRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getMvList(String singerMid,int pageIndex){
        pageIndex=pageIndex<0?0:pageIndex;
        localSingerMid.set(singerMid);
        localPageIndex.set(pageIndex);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        MvListRequestParam mvListRequestParam=new MvListRequestParam();
        mvListRequestParam.setSingerMid(localSingerMid.get());
        int pageIndex=localPageIndex.get();
        int start=pageIndex<2?0:(pageIndex-1)*PAGE_SIZE;
        mvListRequestParam.setStart(start);
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,mvListRequestParam);
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
    private class MvListRequestParam{
        @JsonProperty("singermid")
        private String singerMid;
        private int count=PAGE_SIZE;
        private int start;
    }
}
