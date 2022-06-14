package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/15
 * @Created by fanduanjin
 */
public abstract class AbstractMvInfoRequest<T> extends AbstractDebugRequest<T> {

    private static final String GROUP="mv_info";
    private static final String MODULE="video.VideoDataServer";
    private static final String METHOD="get_video_info_batch";
    private final ThreadLocal<String[]> localMvMid=new ThreadLocal();
    private PenguinRequestParameterCreator penguinRequestParameterCreator;

    public AbstractMvInfoRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getMvInfo(String... mvMid){
        localMvMid.set(mvMid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        MvInfoRequestParam mvInfoRequestParam=new MvInfoRequestParam();
        mvInfoRequestParam.setVidList(localMvMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE,METHOD,mvInfoRequestParam);
    }


    @Override
    protected void finallyProcessor() {
        localMvMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    public class MvInfoRequestParam{

        @JsonProperty("vidlist")
        private String [] vidList;
        //private String [] required={"vid","type","sid","cover_pic","duration","singers","new_switch_str","video_pay",
       //         "hint","code","msg","name","desc","playcnt","pubdate","isfav","fileid","filesize","pay","pay_info",
        //       "uploader_headurl","uploader_nick","uploader_uin","uploader_encuin"};
        private String [] required={"desc","name","playcnt","pubdate","sid","singers","vid"};
    }
}
