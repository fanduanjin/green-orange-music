package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/15
 * @Created by fanduanjin
 */
public class MvUrlRequest extends AbstractDebugRequest<String> {

    private static final String GROUP="mvUrl";
    private static final String MODULE="music.stream.MvUrlProxy";
    private static final String METHOD="GetMvUrls";

    public MvUrlRequest(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        return null;
    }

    @Override
    protected String convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        return null;
    }

    @Override
    protected void finallyProcessor() {

    }

    @Override
    protected String getGroup() {
        return GROUP;
    }
}
