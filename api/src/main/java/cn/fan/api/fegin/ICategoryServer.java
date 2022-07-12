package cn.fan.api.fegin;

import cn.fan.model.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fanduanjin
 */
@FeignClient("server-music")
public interface ICategoryServer {

    /**
     * 同步分类信息
     * @param categories
     * @return
     */
    @RequestMapping("/doSyncCategory")
    String doSyncCategory(@RequestBody List<Category> categories);

}
