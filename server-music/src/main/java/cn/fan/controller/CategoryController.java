package cn.fan.controller;

import cn.fan.api.fegin.ICategoryServer;
import cn.fan.model.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/8
 * @Created by fanduanjin
 */
@RestController
public class CategoryController implements ICategoryServer {

    @Override
    public String doSyncCategory(List<Category> categories) {
        return "调用成功";
    }
}
