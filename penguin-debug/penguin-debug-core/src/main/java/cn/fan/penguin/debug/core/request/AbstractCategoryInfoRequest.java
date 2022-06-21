package cn.fan.penguin.debug.core.request;

import cn.fan.penguin.debug.core.http.DebugResult;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author fanduanjin
 * @Description 利用http请求 去截取字符串json
 * @Date 2022/6/22
 * @Created by fanduanjin
 */
public abstract class AbstractCategoryInfoRequest<T> {

    private ObjectMapper objectMapper;

    public AbstractCategoryInfoRequest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DebugResult<T> getCategoryInfo() {

        try {
            String html = HttpUtil.get("http://y.qq.com/n/ryqq/category");
            html = html.substring(html.indexOf("</body>") + 7);
            html = html.substring(html.indexOf('{'), html.indexOf("</script>"));
            JsonNode dataNode = objectMapper.readTree(html);
            return DebugResult.successResult(convertCategories(dataNode));
        } catch (Exception e) {
            return DebugResult.failedResult(e.toString());
        }
    }

    /**
     * 将dateNode 转换POJO
     *
     * @param dataNode
     * @return
     */
    protected abstract T convertCategories(JsonNode dataNode);
}
