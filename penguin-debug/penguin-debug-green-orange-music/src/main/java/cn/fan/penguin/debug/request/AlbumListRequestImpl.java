package cn.fan.penguin.debug.request;

import cn.fan.model.Album;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractAlbumListRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public class AlbumListRequestImpl extends AbstractAlbumListRequest<List<Album>> {
    public AlbumListRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected List<Album> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        JsonNode albumListNode= dataNode.get("albumList");
        List<Album> albums=new ArrayList<>(albumListNode.size());
        Album album;
        for(JsonNode albumNode: albumListNode){
            album=new Album();
            album.setId(albumNode.get("albumID").asLong());
            album.setName(albumNode.get("albumName").asText());
            album.setMid(albumNode.get("albumMid").asText());
            albums.add(album);
        }
        return albums;
    }
}
