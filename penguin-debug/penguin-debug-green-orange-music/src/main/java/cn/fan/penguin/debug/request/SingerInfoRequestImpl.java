package cn.fan.penguin.debug.request;

import cn.fan.model.$enum.SingerType;
import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSingerInfoRequest;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/8
 * @Created by fanduanjin
 */
public class SingerInfoRequestImpl extends AbstractSingerInfoRequest<List<Singer>> {
    public SingerInfoRequestImpl(PenguinRequestParameterCreator penguinRequestCreator) {
        super(penguinRequestCreator);
    }

    @Override
    protected List<Singer> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        JsonNode singerListNode = dataNode.get("singer_list");
        if (singerListNode == null || singerListNode.isEmpty()) {
            return ListUtil.empty();
        }
        List<Singer> singers = new ArrayList<>(singerListNode.size());
        Singer singer;
        for (JsonNode singerNode : singerListNode) {
            singer = prepareDataNodeItem(singerNode);
            singers.add(singer);
        }
        return singers;
    }


    private Singer prepareDataNodeItem(JsonNode singerItemNode) {

        Singer singer = new Singer();
        //basic_info解析
        JsonNode basicInfoNode = singerItemNode.get("basic_info");
        singer.setMid(basicInfoNode.get("singer_mid").asText());
        singer.setName(basicInfoNode.get("name").asText());
        singer.setType(SingerType.valueOf(basicInfoNode.get("type").asInt()));
        singer.setId(basicInfoNode.get("singer_id").asLong());
        //ex_info解析
        JsonNode exInfoNode = singerItemNode.get("ex_info");
        singer.setDesc(exInfoNode.get("desc").asText());
        //singer.setGenre(exInfoNode.get("genre").asInt());
        singer.setForeignName(exInfoNode.get("foreign_name").asText());
        String birthdayStr = exInfoNode.get("birthday").asText();
        if (birthdayStr.length() == 10) {
            singer.setBirthday(DateUtil.parse(birthdayStr));
        } else if (!StrUtil.isEmpty(birthdayStr)) {
            //按照-切分 索引位 0年 1月 2日
            String[] birthdayArr = birthdayStr.split("-");
            String year = birthdayArr[0];
            if (year.length() > 4) {
                //19971 取 1997
                year = year.substring(0, 4);
            } else if (year.length() < 4) {
                year += "0";
            }
            birthdayArr[0] = year;
            String month, day;
            if (birthdayArr.length < 3) {
                month = "01";
                day = "01";
                birthdayArr=new String[]{year,month,day};
{}            } else {
                month = birthdayArr[1];
                day = birthdayArr[2];
            }
            singer.setBirthday(DateUtil.parse(String.join("-", birthdayArr)));
        }
        //wiki解析
        singer.setWiki(singerItemNode.get("wiki").asText());
        //pic解析
        singer.setPic(singerItemNode.get("pic").get("pic").asText());
        //group_list
        if (singer.getType() == SingerType.TEAM) {
            JsonNode groupListNode = singerItemNode.get("group_list");
            singer.setSingers(new ArrayList<>(groupListNode.size()));
            groupListNode.forEach(jsonNode -> {
                Singer teamSinger = new Singer();
                teamSinger.setId(jsonNode.get("singer_id").asLong());
                teamSinger.setMid(jsonNode.get("singer_mid").asText());
                teamSinger.setName(jsonNode.get("name").asText());
                singer.getSingers().add(teamSinger);
            });
        }
        return singer;
    }


}
