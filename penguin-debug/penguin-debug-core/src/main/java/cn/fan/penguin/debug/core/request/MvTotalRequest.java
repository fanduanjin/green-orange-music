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
public class MvTotalRequest  extends AbstractDebugRequest<Integer> {

    private static final String GROUP="mv_list";
    private static final String MODULE="MvService.MvInfoProServer";
    private static final String METHOD="GetSingerMvList";
    private static final int PAGE_SIZE=50;

    private final ThreadLocal<String> localSingerMid=new ThreadLocal<>();

    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    public MvTotalRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<Integer> getTotal(String singerMid){
        localSingerMid.set(singerMid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        MvTotalRequestParam mvTotalRequestParam=new MvTotalRequestParam();
        mvTotalRequestParam.setSingerMid(localSingerMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,mvTotalRequestParam);
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
    private class MvTotalRequestParam{
        @JsonProperty("singermid")
        private String singerMid;
        private int count=0;
        private int start=0;
    }
}
