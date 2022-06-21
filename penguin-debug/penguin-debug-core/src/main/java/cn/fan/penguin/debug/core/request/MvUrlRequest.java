package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.model.MvUrlInfo;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/15
 * @Created by fanduanjin
 */
public class MvUrlRequest extends AbstractDebugRequest<List<MvUrlInfo>> {
    private static final String MV_FILE_SERVER_Host = "mv.music.tc.qq.com/";

    private static final String GROUP = "mvUrl";
    private static final String MODULE = "music.stream.MvUrlProxy";
    private static final String METHOD = "GetMvUrls";
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    private final ThreadLocal<String[]> localMid = new ThreadLocal<>();

    public MvUrlRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator = penguinRequestParameterCreator;
    }

    public DebugResult<List<MvUrlInfo>> getMvUrls(String... mid) {
        localMid.set(mid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        MvUrlRequestParam urlRequestParam = new MvUrlRequestParam();
        urlRequestParam.setMids(localMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP, MODULE, METHOD, urlRequestParam);
    }

    @Override
    protected List<MvUrlInfo> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        try {
            String str = objectMapper.writeValueAsString(dataNode);
            System.out.println(str);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Iterator<String> midIterator = dataNode.fieldNames();
        String mid;
        MvUrlInfo mvUrlInfo;
        List<MvUrlInfo> mvUrlInfos = new ArrayList<>(dataNode.size());
        JsonNode mvUrlItem;
        while (midIterator.hasNext()) {
            mid = midIterator.next();
            mvUrlInfo = new MvUrlInfo();
            mvUrlInfo.setMid(mid);
            mvUrlItem = dataNode.get(mid);
            preparedMvUrlInfo(mvUrlItem,mvUrlInfo);
            mvUrlInfos.add(mvUrlInfo);
        }
        return mvUrlInfos;
    }

    @Override
    protected void finallyProcessor() {
        localMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class MvUrlRequestParam {
        //{"vids":["i0043xhgczd"],"request_type":10003,"addrtype":3,"format":264}
        @JsonProperty("vids")
        private String[] mids;
    }

    private void preparedMvUrlInfo(JsonNode itemNode, MvUrlInfo mvUrlInfo) {
        JsonNode mp4Node = itemNode.get("mp4");
        JsonNode mp4ItemNode;
        //标清
        mp4ItemNode=mp4Node.get(1);
        mvUrlInfo.setP360(preparedMp4NodeItem(mp4ItemNode));
        //高清
        mp4ItemNode=mp4Node.get(2);
        mvUrlInfo.setP480(preparedMp4NodeItem(mp4ItemNode));
        //超清
        mp4ItemNode=mp4Node.get(3);
        mvUrlInfo.setP720(preparedMp4NodeItem(mp4ItemNode));
        //全高清
        mp4ItemNode=mp4Node.get(4);
        mvUrlInfo.setP1080(preparedMp4NodeItem(mp4ItemNode));
    }

    private String preparedMp4NodeItem(JsonNode mp4ItemNode) {
        if (mp4ItemNode == null) {
            return StrUtil.EMPTY;
        }
        String vkey=mp4ItemNode.get("vkey").asText();
        if(StrUtil.isEmpty(vkey)){
            return StrUtil.EMPTY;
        }
        return MV_FILE_SERVER_Host + vkey +"/"+ mp4ItemNode.get("cn").asText();
    }

}
