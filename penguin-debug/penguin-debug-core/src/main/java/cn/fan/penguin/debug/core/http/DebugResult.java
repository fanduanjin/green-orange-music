package cn.fan.penguin.debug.core.http;

import lombok.Data;

/**
 * @author fanduanjin
 * @program debug-music-sdk
 * @Classname
 * @Description
 * @Date 2022/5/11
 * @Created by fanduanjin
 */
@Data
public class DebugResult<T> {

    private boolean success;

    private T data;

    private String message;


    public static <T> DebugResult<T> successResult(T data) {
        DebugResult debugResult = new DebugResult();
        debugResult.setData(data);
        debugResult.setSuccess(true);
        return debugResult;
    }

    public static <T> DebugResult<T> failedResult(String message) {
        DebugResult debugResult = new DebugResult();
        debugResult.setMessage(message);
        debugResult.setSuccess(false);
        return debugResult;
    }

    public static <T> DebugResult<T> failedCodeResult(Class clazz) {
        DebugResult debugResult = new DebugResult();
        debugResult.setMessage("debug请求状态码错误 : " + clazz.getName());
        debugResult.setSuccess(false);
        return debugResult;
    }

    public static <T> DebugResult<T> failedEmptyResult(Class clazz) {
        DebugResult debugResult = new DebugResult();
        debugResult.setMessage("debug请求 empty 资源 : " + clazz.getName());
        debugResult.setSuccess(false);
        return debugResult;
    }


}
