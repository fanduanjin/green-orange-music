package cn.fan.penguin.debug.request;

import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSingerListRequest;
import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class SingerListRequestImpl extends AbstractSingerListRequest<List<Singer>> {

    public SingerListRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected List<Singer> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode)  {
        JsonNode singerListNode = dataNode.get("singerlist");
        int singerNums = singerListNode.size();
        if (singerNums == 0) {
            return ListUtil.empty();
        }
        List<Singer> singers = new ArrayList<>(singerNums);
        JsonNode singerNode;
        Singer singer;
        for (int i = 0; i < singerNums; i++) {
            singerNode = singerListNode.get(i);
            singer = new Singer();
            singer.setId(singerNode.get("singer_id").asLong());
            singer.setMid(singerNode.get("singer_mid").asText());
            singer.setName(singerNode.get("singer_name").asText());
            singers.add(singer);
        }
        return singers;
    }

}
