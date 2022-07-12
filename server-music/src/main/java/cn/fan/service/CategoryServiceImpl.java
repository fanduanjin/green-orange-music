package cn.fan.service;

import cn.fan.api.service.ICategoryService;
import cn.fan.dao.CategoryRepository;
import cn.fan.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/8
 * @Created by fanduanjin
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(int id) {
        return categoryRepository.getCategoryById(id);
    }
}
