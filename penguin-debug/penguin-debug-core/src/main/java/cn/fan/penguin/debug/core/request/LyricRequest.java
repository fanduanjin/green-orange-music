package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/10
 * @Created by fanduanjin
 */
public class LyricRequest extends AbstractDebugRequest<String> {

    private static final String LYRIC_REQUEST_URL = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new" +
            ".fcg?format=json&songmid=";
    private final ThreadLocal<String> localSongMid = new ThreadLocal<>();

    public LyricRequest(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public DebugResult<String> getSongLyric(String songMid) {
        localSongMid.set(songMid);
        return request();
    }

    @Override
    protected void finallyProcessor() {
        localSongMid.remove();
    }

    @Override
    protected String getUrl()  {
        return LYRIC_REQUEST_URL + localSongMid.get();
    }

    @Override
    protected String convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        return rootNode.get("lyric").asText();
    }

    @Override
    protected String getGroup() {
        return null;
    }


}
