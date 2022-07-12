package cn.fan.penguin.debug.request;

import cn.fan.model.Category;
import cn.fan.penguin.debug.core.request.AbstractCategoryInfoRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/22
 * @Created by fanduanjin
 */
@Deprecated
public class CategoryInfoRequestImpl extends AbstractCategoryInfoRequest<List<Category>> {
    public CategoryInfoRequestImpl(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected List<Category> convertCategories(JsonNode dataNode) {
        if(dataNode==null||dataNode.isEmpty()){
            return Collections.emptyList();
        }
        List<Category> categories=new ArrayList<>();
        JsonNode categoriesNode=dataNode.get("categories");
        JsonNode categoryGroupItemNode;
        Category category;
        for(JsonNode categoryGroupNode:categoriesNode){
            Category groupCategory=new Category();
            groupCategory.setId(categoryGroupNode.get("groupId").asInt());
            groupCategory.setName(categoryGroupNode.get("categoryGroupName").asText());
            categories.add(groupCategory);
            //获取items 节点
            categoryGroupItemNode=categoryGroupNode.get("items");
            //遍历items节点
            if(categoryGroupItemNode==null||categoryGroupItemNode.isEmpty()){
                //items节点没有数据跳过当前分组
                continue;
            }
            for(JsonNode categoryNode:categoryGroupItemNode){
                category=new Category();
                category.setId(categoryNode.get("categoryId").asInt());
                category.setName(categoryNode.get("categoryName").asText());
                category.setGroupId(groupCategory.getId());
                categories.add(category);
            }
        }
        return categories;
    }
}
