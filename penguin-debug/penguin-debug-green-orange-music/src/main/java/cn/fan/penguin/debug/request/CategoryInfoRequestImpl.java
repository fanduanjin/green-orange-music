package cn.fan.penguin.debug.request;

import cn.fan.penguin.debug.core.request.AbstractCategoryInfoRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/22
 * @Created by fanduanjin
 */
public class CategoryInfoRequestImpl extends AbstractCategoryInfoRequest<Object> {
    public CategoryInfoRequestImpl(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected Object convertCategories(JsonNode dataNode) {
        System.out.println(dataNode);
        return null;
    }
}
