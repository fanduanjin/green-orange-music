package cn.fan.api.service;

import cn.fan.model.Singer;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/8
 * @Created by fanduanjin
 */

public interface ISingerService {

    /**
     * 添加一个歌手
     * @param singer
     * @return
     */
    Singer addSinger(Singer singer);

    /**
     * 根据id查询歌手信息
     * @param id
     * @return
     */
    Singer getSingerById(Long id);
}
