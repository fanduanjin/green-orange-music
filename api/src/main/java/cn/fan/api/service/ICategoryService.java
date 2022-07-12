package cn.fan.api.service;

import cn.fan.model.Category;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/8
 * @Created by fanduanjin
 */
public interface ICategoryService {

    /**
     * 插入一个分类
     * @param category
     * @return
     */
    Category addCategory(Category category);

    /**
     * 根据id获取分类
     * @param id
     * @return
     */
    Category getCategory(int id);
}
