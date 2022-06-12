package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public abstract class AbstractSingerListRequest<T> extends AbstractDebugRequest<T> {

    private static final String Module = "Music.SingerListServer";
    private static final String METHOD = "get_singer_list";
    private static final String GROUP = "singer_list";
    public static final Integer SIN_OFFSET = 80;
    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    private ThreadLocal<Integer> localPageIndex = new ThreadLocal<>();


    public AbstractSingerListRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator = penguinRequestParameterCreator;
    }

    public DebugResult<T> getSingerList(Integer pageIndex) {
        if (pageIndex == null) {
            pageIndex = 1;
        }
        localPageIndex.set(pageIndex);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        SingerListRequestParam singerListRequestParam = new SingerListRequestParam();
        singerListRequestParam.curPage = localPageIndex.get();
        if (singerListRequestParam.curPage > 1) {
            singerListRequestParam.sin = (singerListRequestParam.curPage - 1) * SIN_OFFSET;
        } else {
            singerListRequestParam.sin = 0;
        }
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP, Module, METHOD, singerListRequestParam);
    }

    @Override
    protected void finallyProcessor() {
        localPageIndex.remove();
    }


    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    private class SingerListRequestParam {
        @JsonProperty("cur_page")
        private Integer curPage;
        private Integer sin;
        private int area = -100;
        private int sex = -100;
        private int genre = -100;
        private int index = -100;
    }
}
