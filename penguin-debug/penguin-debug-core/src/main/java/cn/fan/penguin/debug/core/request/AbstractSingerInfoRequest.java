package cn.fan.penguin.debug.core.request;


import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author fanduanjin
 * @program debug-music-sdk
 * @Classname
 * @Description
 * @Date 2022/5/11
 * @Created by fanduanjin
 */
public abstract class AbstractSingerInfoRequest<T> extends AbstractDebugRequest<T> {

    private static final String PREFIX_PATH = "";
    protected static final String MODULE = "music.musichallSinger.SingerInfoInter";
    private static final String METHOD = "GetSingerDetail";
    private static final String GROUP = "singer_detail";
    private  final ThreadLocal<String[]> localMid = new ThreadLocal<>();

    private PenguinRequestParameterCreator requestParameterCreator;

    public AbstractSingerInfoRequest(PenguinRequestParameterCreator penguinRequestCreator) {
        super( penguinRequestCreator.getObjectMapper());
        this.requestParameterCreator = penguinRequestCreator;
    }

    public DebugResult<T> getSingerInfoResult(String... singerMid ) {
        localMid.set(singerMid);
        return request();
    }


    @Override
    public String getUrl() throws JsonProcessingException {
        SingerInfoRequestParam param = new SingerInfoRequestParam();
        param.setSingerMids(localMid.get());
        return requestParameterCreator.getBasicRequestUrl(GROUP, MODULE, METHOD, param);
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
    private  class SingerInfoRequestParam {
        //{ "singer_mids": [], "ex_singer": 1, "wiki_singer": 1, "group_singer": 1, "pic": 1, "photos": 0 }
        @JsonProperty("singer_mids")
        private String[] singerMids;
        @JsonProperty("ex_singer")
        private int exSinger = 1;
        @JsonProperty("wiki_singer")
        private int wikiSinger = 1;
        @JsonProperty("group_singer")
        private int groupSinger = 1;
        private int pic = 1;
        private int photos = 0;
    }
}
