package cn.fan.dao;

import cn.fan.model.Singer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;


/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/25
 * @Created by fanduanjin
 */

public interface SingerRepository extends Repository<Singer, Long> {

    /**
     * 根据id查询singer
     * @param id
     * @return
     */
    Singer getSingerById(Long id);

    /**
     * 插入一个singer
     * @param singer
     * @return
     */
    Singer save(Singer singer);
}
