package cn.fan.model;

import cn.fan.model.Song;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
@Data
public class SongOrder {
    private long id;
    private String name;
    private String desc;
    private Date createDate;
    /**
     * 播放数量
     */
    private long playNum;
    /**
     * 收藏数量
     */
    private long collectNum;
    List<Song> songs;
}
