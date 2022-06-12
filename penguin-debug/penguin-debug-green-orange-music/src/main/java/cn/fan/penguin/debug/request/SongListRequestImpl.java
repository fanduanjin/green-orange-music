package cn.fan.penguin.debug.request;

import cn.fan.model.Song;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSongListRequest;
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
public class SongListRequestImpl extends AbstractSongListRequest<List<Song>> {
    public SongListRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected List<Song> convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode)  {
        JsonNode songListNode = dataNode.get("songList");
        int songListNum = songListNode.size();
        if (songListNum == 0) {
            return ListUtil.empty();
        }
        List<Song> songs = new ArrayList<>(songListNum);
        Song song;
        JsonNode songNode;
        JsonNode songInfoNode;
        for (int i = 0; i < songListNum; i++) {
            songNode = songListNode.get(i);
            songInfoNode = songNode.get("songInfo");
            song = new Song();
            song.setId(songInfoNode.get("id").asLong());
            song.setMid(songInfoNode.get("mid").asText());
            song.setName(songInfoNode.get("name").asText());
            songs.add(song);
        }
        return songs;
    }
}
