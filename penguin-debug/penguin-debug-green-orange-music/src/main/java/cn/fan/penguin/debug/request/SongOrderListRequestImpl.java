package cn.fan.penguin.debug.request;

import cn.fan.model.Song;
import cn.fan.model.SongOrder;
import cn.fan.penguin.debug.core.exception.CodeStateException;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSongOrderListRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
public class SongOrderListRequestImpl extends AbstractSongOrderListRequest<List<Song>> {
    public SongOrderListRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected List<Song> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        if (dataNode.get("code").asInt() != 0 || dataNode.get("subcode").asInt() != 0) {
            throw new CodeStateException("songOrderInfoRequest failed : " + localSongOrderId.get());
        }
        JsonNode songListNode = dataNode.get("songlist");
        List<Song> songs = new ArrayList<>(songListNode.size());
        Song song;
        for (JsonNode songNode : songListNode) {
            song = new Song();
            song.setId(songNode.get("id").asLong());
            song.setName(songNode.get("name").asText());
            song.setTitle(songNode.get("title").asText());
            song.setMid(songNode.get("mid").asText());
            songs.add(song);
        }
        return songs;
    }
}
