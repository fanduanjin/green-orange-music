package cn.fan.dao;

import cn.fan.model.Singer;


/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/25
 * @Created by fanduanjin
 */

//@Mapper
public interface SingerMapper {

    /**
     * 插入一个歌手
     * @param singer
     * @return
     */
    int insert(Singer singer);

    /**
     * 判断歌手是否存在
     * @param id
     * @return
     */
    boolean existsSinger(long id);
}
