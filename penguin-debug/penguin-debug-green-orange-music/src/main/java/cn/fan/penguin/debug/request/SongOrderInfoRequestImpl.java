package cn.fan.penguin.debug.request;

import cn.fan.model.SongOrder;
import cn.fan.penguin.debug.core.exception.CodeStateException;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSongOrderInfoRequest;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public class SongOrderInfoRequestImpl extends AbstractSongOrderInfoRequest<SongOrder> {
    public SongOrderInfoRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected SongOrder convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        if(dataNode.get("code").asInt()!=0||dataNode.get("subcode").asInt()!=0){
            throw new  CodeStateException("songOrderInfoRequest failed : "+localSongOrderId.get());
        }
        JsonNode dirInfoNode=dataNode.get("dirinfo");
        SongOrder songOrder=new SongOrder();
        songOrder.setId(dirInfoNode.get("id").asLong());
        songOrder.setName(dirInfoNode.get("title").asText());
        songOrder.setDesc(dirInfoNode.get("desc").asText());
        songOrder.setCollectNum(dirInfoNode.get("ordernum").asInt());
        songOrder.setPlayNum(dirInfoNode.get("listennum").asInt());
        songOrder.setCreateDate(DateUtil.date(dirInfoNode.get("ctime").asLong()));
        return songOrder;
    }
}
