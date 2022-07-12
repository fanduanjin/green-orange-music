package cn.fan.dao;

import cn.fan.model.Song;
import org.springframework.data.repository.Repository;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
public interface SongRepository extends Repository<Song,Long> {

    /**
     * 根据id获取歌曲
     * @param id
     * @return
     */
    Song getSongById(Long id);

    /**
     * 插入歌曲
     * @param song
     * @return
     */
    Song save(Song song);
}
