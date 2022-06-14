package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.AbstractDebugRequest;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinConstParameter;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.LoadClass;
import lombok.Data;
import org.omg.CosNaming.NamingContextPackage.InvalidNameHolder;

import java.util.Queue;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/15
 * @Created by fanduanjin
 */
public abstract class AbstractAlbumInfoRequest<T> extends AbstractDebugRequest<T> {

    private static final String GROUP = "album_info";
    private static final String MODULE = "music.musichallAlbum.AlbumInfoServer";
    private static final String METHOD = "GetAlbumDetail";

    private PenguinRequestParameterCreator penguinRequestParameterCreator;
    private final ThreadLocal<String> localAlbumMid = new ThreadLocal<>();

    public AbstractAlbumInfoRequest(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator.getObjectMapper());
        this.penguinRequestParameterCreator=penguinRequestParameterCreator;
    }

    public DebugResult<T> getAlbumInfo(String albumMid) {
        localAlbumMid.set(albumMid);
        return request();
    }

    @Override
    protected String getUrl() throws JsonProcessingException {
        AlbumInfoRequestParam albumInfoRequestParam=new AlbumInfoRequestParam();
        albumInfoRequestParam.setAlbumMid(localAlbumMid.get());
        return penguinRequestParameterCreator.getBasicRequestUrl(GROUP,MODULE, METHOD,albumInfoRequestParam);
    }


    @Override
    protected void finallyProcessor() {
        localAlbumMid.remove();
    }

    @Override
    protected String getGroup() {
        return GROUP;
    }

    @Data
    public class AlbumInfoRequestParam {
        private String albumMid;
        //private long albumID;
    }
}
