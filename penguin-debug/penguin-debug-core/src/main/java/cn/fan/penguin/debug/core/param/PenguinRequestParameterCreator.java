package cn.fan.penguin.debug.core.param;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fanduanjin
 * @program debug-music-sdk
 * @Classname
 * @Description
 * @Date 2022/5/11
 * @Created by fanduanjin
 */
public class PenguinRequestParameterCreator {
    private final Comm comm = new Comm();

    @Getter
    private ObjectMapper objectMapper;

    public PenguinRequestParameterCreator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public PenguinRequestGroupParameter createGroupParameter(String module, String method, Object param) {
        return new PenguinRequestGroupParameter(module, method, param);
    }


    public Map<String, Object> createBasicParameterMap(String group, String module, String method, Object param)  {
        PenguinRequestGroupParameter groupParameter = createGroupParameter(module, method, param);
        Map<String, Object> requestMap = new HashMap<>(2);
        requestMap.put(group, groupParameter);
        requestMap.put("comm", comm);
        return requestMap;
    }

    /**
     * 获取请求签名
     *
     * @param param
     * @return
     */
    public String getSign(String param) {
        return PenguinConstParameter.EN_PREFIX + RandomUtil.randomString(16) + SecureUtil.md5(PenguinConstParameter.ENC_NONCE + param);
    }

    public String getBasicRequestUrl(String group, String module, String method, Object param) throws JsonProcessingException {
        Map<String, Object> requestParameterMap = createBasicParameterMap(group, module, method, param);
        String requestParameterStr = objectMapper.writeValueAsString(requestParameterMap);
        return getBasicRequestUrl(requestParameterStr);
    }

    private String getBasicRequestUrl(String requestParameter) {
        return StrUtil.format(PenguinConstParameter.BASIC_URL_TEMPLATE, requestParameter, getSign(requestParameter));
    }

    @Data
    private class Comm {
        private int ct = 24;
        /**
         * 10-16位随机数
         */
        private int cv;

        Comm() {
            this.cv = RandomUtil.randomInt(10, 16);
        }
    }
}
