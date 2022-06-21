package cn.fan.penguin.debug.request;

import cn.fan.model.Mv;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractMvListRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public class MvListRequestImpl extends AbstractMvListRequest<List<Mv>> {
    public MvListRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected List<Mv> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        JsonNode listNode = dataNode.get("list");
        List<Mv> mvs = new ArrayList<>(listNode.size());
        Mv mv;
        for(JsonNode mvNode:listNode){
            mv=new Mv();
            mv.setId(mvNode.get("mvid").asLong());
            mv.setMid(mvNode.get("vid").asText());
            mv.setName(mvNode.get("title").asText());
            mvs.add(mv);
        }
        return mvs;
    }
}
