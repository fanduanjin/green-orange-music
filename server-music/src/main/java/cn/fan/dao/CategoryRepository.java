package cn.fan.dao;

import cn.fan.model.Category;
import cn.fan.model.Singer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/7
 * @Created by fanduanjin
 */
public interface CategoryRepository extends Repository<Category,Integer> {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Category getCategoryById(Integer id);

    /**
     * 插入一个分类
     */
    Category save(Category category);
}
