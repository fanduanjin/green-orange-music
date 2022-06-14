package cn.fan.penguin.debug.request;

import cn.fan.model.Album;
import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractAlbumInfoRequest;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/15
 * @Created by fanduanjin
 */
public class AlbumInfoRequestImpl extends AbstractAlbumInfoRequest<Album> {
    private static final String PREPARE_CATEGORY_PREFIX="?categoryId=";

    public AlbumInfoRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected Album convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        JsonNode basicInfoNode=dataNode.get("basicInfo");
        Album album=new Album();
        album.setMid(basicInfoNode.get("albumMid").asText());
        album.setName(basicInfoNode.get("albumName").asText());
        JsonNode tranNameNode=basicInfoNode.get("tranName");
        album.setTranName(tranNameNode==null? StrUtil.EMPTY:tranNameNode.asText());
        album.setPublishTime(DateUtil.parse(basicInfoNode.get("publishDate").asText()));
        album.setDesc(basicInfoNode.get("desc").asText());
        JsonNode genreURLNode=basicInfoNode.get("genreURL");
        album.setGenreId(prepareUlrCategory(genreURLNode.asText()));
        JsonNode lanURLNode=basicInfoNode.get("lanURL");
        album.setLanguageId(prepareUlrCategory(lanURLNode.asText()));
        album.setId(basicInfoNode.get("albumID").asLong());
        JsonNode singerListNode=dataNode.get("singer").get("singerList");
        album.setSingers(new ArrayList<>(singerListNode.size()));
        List<Singer> singers=album.getSingers();
        Singer singer;
        for(JsonNode singerNode:singerListNode){
            singer=new Singer();
            singer.setId(singerNode.get("singerID").asLong());
            singer.setMid(singerNode.get("mid").asText());
            singer.setName(singerNode.get("name").asText());
            singers.add(singer);
        }
        return album;
    }


    protected int prepareUlrCategory(String categoryUrl){
        if(categoryUrl.equals(StrUtil.EMPTY)){
            return 0;
        }
        //先拿到？后面参数
        categoryUrl=categoryUrl.substring(categoryUrl.indexOf(PREPARE_CATEGORY_PREFIX));
        //在截取第一个&符号
        categoryUrl=categoryUrl.substring(PREPARE_CATEGORY_PREFIX.length(),categoryUrl.indexOf("&"));
        //转换成int
        return Integer.valueOf(categoryUrl);
    }
}
