package cn.fan.penguin.debug.request;

import cn.fan.model.*;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.AbstractSongInfoRequest;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class SongInfoRequestImpl extends AbstractSongInfoRequest<Song> {
    public SongInfoRequestImpl(PenguinRequestParameterCreator penguinRequestParameterCreator) {
        super(penguinRequestParameterCreator);
    }

    @Override
    protected Song convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode) {
        Song song = new Song();
        JsonNode infoNode = dataNode.get("info");
        //company
        JsonNode companyNode = infoNode.get("company");
        if (companyNode != null) {
            JsonNode companyContentNode = companyNode.get("content").get(0);
            Company company = new Company();
            company.setId(companyContentNode.get("id").asInt());
            company.setValue(companyContentNode.get("value").asText());
            song.setCompanyId(company.getId());
            song.setCompany(company);
        }
        //genre
        JsonNode genreNode = infoNode.get("genre");
        if (genreNode != null) {
            JsonNode genreContentNode = genreNode.get("content").get(0);
            Genre genre = new Genre();
            genre.setId(genreContentNode.get("id").asInt());
            genre.setValue(genreContentNode.get("value").asText());
            song.setGenreId(genre.getId());
            song.setGenre(genre);
        }
        //lan
        JsonNode lanNode = infoNode.get("lan").get("content").get(0);
        Language language = new Language();
        language.setId(lanNode.get("id").asInt());
        language.setValue(lanNode.get("value").asText());
        song.setLanguage(language);
        //track_info
        JsonNode trackInfoNode = dataNode.get("track_info");
        song.setId(trackInfoNode.get("id").asLong());
        song.setMid(trackInfoNode.get("mid").asText());
        song.setName(trackInfoNode.get("name").asText());
        song.setTitle(trackInfoNode.get("title").asText());
        song.setSubTitle(trackInfoNode.get("subtitle").asText());
        //album
        JsonNode albumNode = trackInfoNode.get("album");
        Album album = new Album();
        album.setId(albumNode.get("id").asLong());
        album.setMid(albumNode.get("mid").asText());
        album.setName(albumNode.get("name").asText());
        song.setAlbumId(album.getId());
        song.setAlbum(album);
        //mv
        JsonNode mvNode = trackInfoNode.get("mv");
        Mv mv = new Mv();
        mv.setId(mvNode.get("id").asLong());
        mv.setVid(mvNode.get("vid").asText());
        song.setMvId(mv.getId());
        song.setMv(mv);
        //time_public
        JsonNode publicTimeNode = trackInfoNode.get("time_public");
        song.setPublicTime(DateUtil.parse(publicTimeNode.asText()));
        //singer
        JsonNode singerNode = trackInfoNode.get("singer");
        song.setSingers(new ArrayList<>(singerNode.size()));
        singerNode.forEach(jsonNode -> {
            Singer singer = new Singer();
            singer.setId(jsonNode.get("id").asLong());
            singer.setMid(jsonNode.get("mid").asText());
            singer.setName(jsonNode.get("name").asText());
            song.getSingers().add(singer);
        });
        // DTO pay
        return song;
    }
}
