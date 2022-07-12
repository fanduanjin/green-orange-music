package cn.fan.service;

import cn.fan.api.service.ISongService;
import cn.fan.dao.SongRepository;
import cn.fan.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/10
 * @Created by fanduanjin
 */
@Service
public class SongServiceImpl implements ISongService {

    @Autowired
    SongRepository songRepository;

    @Override
    public Song getSongById(Long id) {
        return songRepository.getSongById(id);
    }

    @Override
    public Song addSong(Song song) {
        return songRepository.save(song);
    }
}
