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
public class SingerTotalRequest extends AbstractDebugRequest<Integer> {
    private static final String Module = "Music.SingerListServer";
    private static final String METHOD = "get_singer_list";
    private static final String GROUP = "singer_total";

    private final PenguinRequestParameterCreator penguinRequestParameterCreator;

    public SingerTotalRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator = penguinRequestParameterCreator;
    }

    public DebugResult<Integer> getSingerTotal() {
        return request();
    }

    @Override
    protected String getUrl() throws  JsonProcessingException {
        SingerTotalRequestParam singerTotalRequestParam = new SingerTotalRequestParam();
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP, Module, METHOD, singerTotalRequestParam);
    }

    @Override
    protected Integer convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode)  {
        return dataNode.get("total").asInt();
    }

    @Override
    protected void finallyProcessor() {

    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class SingerTotalRequestParam {
        @JsonProperty("cur_page")
        private Integer curPage = 1;
        private Integer sin = 0;
        private int area = -100;
        private int sex = -100;
        private int genre = -100;
        private int index = -100;
    }
}
