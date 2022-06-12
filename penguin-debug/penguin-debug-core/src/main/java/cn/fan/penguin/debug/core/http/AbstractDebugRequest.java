package cn.fan.penguin.debug.core.http;

import cn.fan.penguin.debug.core.exception.CodeStateException;
import cn.fan.penguin.debug.core.tools.DebugCodeTool;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author fanduanjin
 * @program debug-music-sdk
 * @Classname
 * @Description
 * @Date 2022/5/11
 * @Created by fanduanjin
 */
public abstract class AbstractDebugRequest<T> {

    protected ObjectMapper objectMapper;

    public AbstractDebugRequest(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected DebugResult<T> request() {
        DebugResult<T> debugResult;
        try {
            HttpRequest request = HttpUtil.createRequest(getMethod(), getUrl());
            modifyRequest(request);
            HttpResponse response = request.execute();
            //获取根节点
            JsonNode rootNode = objectMapper.readTree(response.body());
            String group = getGroup();
            if (DebugCodeTool.isSuccessCode(rootNode, getGroup()) == false) {
                //说明这次爬取数据 返回的code 有问题
                throw new CodeStateException(request.getUrl());
            }

            JsonNode groupNode = null;
            JsonNode dataNode = null;
            if (group != null) {
                groupNode = rootNode.get(getGroup());
                dataNode = groupNode.get("data");
            }
            T entity = convertGroupNode(rootNode, groupNode, dataNode);
            debugResult = DebugResult.successResult(entity);
        } catch (CodeStateException codeStateException) {
            debugResult = DebugResult.failedResult(codeStateException.toString());
        } catch (JsonMappingException e) {
            debugResult = DebugResult.failedResult(e.toString());
        } catch (JsonProcessingException e) {
            debugResult = DebugResult.failedResult(e.toString());
        } finally {
            finallyProcessor();
        }
        return debugResult;
    }


    protected Method getMethod() {
        return Method.GET;
    }

    /**
     * 获取请求Path
     *
     * @return
     */
    protected abstract String getUrl() throws JsonProcessingException;

    /**
     * 子类可以重写这个方法，做一些 请求头 请求体的修改
     */
    protected void modifyRequest(HttpRequest request) {
        request.header("referer", "https://y.qq.com/");
    }


    /**
     * 子类重写这个方法处理响应数据
     *
     * @param rootNode
     * @param groupNode
     * @param dataNode
     * @return
     */
    protected abstract T convertGroupNode(JsonNode rootNode, JsonNode groupNode, JsonNode dataNode);

    /**
     * 不管是否请求成功都会调用 做一些资源释放
     */
    abstract protected void finallyProcessor();

    protected abstract String getGroup();


}
