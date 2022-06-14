package cn.fan.penguin.debug.request;

import cn.fan.model.Mv;
import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractMvInfoRequest;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/15
 * @Created by fanduanjin
 */
public class MvInfoRequestImpl extends AbstractMvInfoRequest<List<Mv>> {
    public MvInfoRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected List<Mv> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        if(dataNode.isEmpty()){
            return ListUtil.empty();
        }
        List<Mv> mvs=new ArrayList<>(dataNode.size());
        JsonNode singersNode;
        Mv mv;
        List<Singer> singers;
        Singer singer;
        for(JsonNode mvNode: dataNode){
            mv=new Mv();
            mv.setId(mvNode.get("sid").asLong());
            mv.setMid(mvNode.get("vid").asText());
            mv.setName(mvNode.get("name").asText());
            mv.setCloudPlayCount(mvNode.get("playcnt").asLong());
            mv.setDesc(mvNode.get("desc").asText());
            mv.setPublishDate(DateUtil.date(mvNode.get("pubdate").asLong()));
            singersNode=mvNode.get("singers");
            singers=new ArrayList<>(singersNode.size());
            mv.setSingers(singers);
            for(JsonNode singerNode:singersNode){
                singer=new Singer();
                singer.setId(singerNode.get("id").asLong());
                singer.setMid(singerNode.get("mid").asText());
                singer.setName(singerNode.get("name").asText());
                singers.add(singer);
            }
            mvs.add(mv);
        }
        return mvs;
    }
}
