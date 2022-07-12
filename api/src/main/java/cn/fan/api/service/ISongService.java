package cn.fan.api.service;


import cn.fan.model.Song;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
public interface ISongService {

    /**
     * 根据id查询歌曲
     * @param id
     * @return
     */
    Song getSongById(Long id);

    /**
     * 添加歌曲
     * @param song
     * @return
     */
    Song addSong(Song song);
}
