package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.model.MediaUrlInfo;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import javafx.beans.binding.StringBinding;
import lombok.Data;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fanduanjin
 * @Description 获取非vip歌曲下载链接
 * @Date 2022/6/10
 * @Created by fanduanjin
 */
public class SongMediaUrlRequest extends AbstractDebugRequest<List<MediaUrlInfo>> {

    /**
     * {"guid":"7911498304","songmid":["000hsEco0QN1do","001BTwv31D0hbb"],"songtype":[0,0],"uin":"0","loginflag":1,
     * "platform":"20"}}
     */

    private static final String GROUP = "media_url";
    private static final String MODULE = "vkey.GetVkeyServer";
    private static final String METHOD = "CgiGetVkey";
    private final ThreadLocal<String[]> localSongMid = new ThreadLocal<>();

    PenguinRequestParameterCreator penguinRequestParameterCreator;

    /**
     * ws.stream.qqmusic.qq.com/
     * dl.stream.qqmusic.qq.com/
     * isure.stream.qqmusic.qq.com/
     */
    private static final String FILE_SERVER_LOCATION = "ws.stream.qqmusic.qq.com/";

    public SongMediaUrlRequest(@NotNull PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator = penguinRequestParameterCreator;
    }

    public DebugResult<List<MediaUrlInfo>> getSongMediaUrl(String... songMid) {
        localSongMid.set(songMid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SongMediaUrlRequestParam songMediaUrlRequestParam = new SongMediaUrlRequestParam();
        songMediaUrlRequestParam.setSongMid(localSongMid.get());
        songMediaUrlRequestParam.setGuid(UUID.fastUUID().toString());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP, MODULE, METHOD, songMediaUrlRequestParam);
    }


    @Override
    protected List<MediaUrlInfo> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode)  {
        JsonNode midUrlInfoNode = dataNode.get("midurlinfo");
        if(midUrlInfoNode==null||midUrlInfoNode.isEmpty()){
            return Collections.emptyList();
        }
        List<MediaUrlInfo> mediaUrlInfos=new ArrayList<>(midUrlInfoNode.size());
        MediaUrlInfo mediaUrlInfo;
        for(JsonNode mediaUrlItemNode:midUrlInfoNode ){
             mediaUrlInfo= prepareMediaUrlItemNode(mediaUrlItemNode);
             mediaUrlInfos.add(mediaUrlInfo);
        }
       return mediaUrlInfos;
    }

    private MediaUrlInfo prepareMediaUrlItemNode(JsonNode mediaUrlItemNode){
        MediaUrlInfo mediaUrlInfo = new MediaUrlInfo();
        mediaUrlInfo.setPurl(mediaUrlItemNode.get("purl").asText());
        if (StrUtil.isEmpty(mediaUrlInfo.getPurl())) {
            mediaUrlInfo.setServerUrl(StrUtil.EMPTY);
        } else {
            mediaUrlInfo.setServerUrl(FILE_SERVER_LOCATION);
        }
        mediaUrlInfo.setFileName(mediaUrlItemNode.get("filename").asText());
        System.out.println(mediaUrlItemNode.toString());
        return mediaUrlInfo;
    }

    @Override
    protected void finallyProcessor() {
        localSongMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class SongMediaUrlRequestParam {
        private String guid;
        @JsonProperty("songmid")
        private String[] songMid;
        //@JsonProperty("songtype")
        //private int[] songType = new int[]{0};

    }


}
