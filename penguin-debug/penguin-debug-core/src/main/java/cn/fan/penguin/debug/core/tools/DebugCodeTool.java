package cn.fan.penguin.debug.core.tools;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/4
 * @Created by fanduanjin
 */
public class DebugCodeTool {

    /**
     *
     * @param rootNode
     * @param group
     * @return true表示状态码正确，可以正常解析数据
     */
    public static boolean isSuccessCode(JsonNode rootNode,String group){
        if(rootNode.get("code").asInt()!=0){
            return false;
        }
        if(group==null){
            return true;
        }
        JsonNode groupNode= rootNode.get(group);
        return groupNode.get("code").asInt()==0;
    }
}
