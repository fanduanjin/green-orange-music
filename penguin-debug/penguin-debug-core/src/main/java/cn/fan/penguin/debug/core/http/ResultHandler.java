package cn.fan.penguin.debug.core.http;

/**
 * @author fanduanjin
 * @program debug-music-sdk
 * @Classname
 * @Description
 * @Date 2022/5/11
 * @Created by fanduanjin
 */
@FunctionalInterface
public interface ResultHandler {

    /**
     * 处理result
     * @param result
     */
     void handlerResult(DebugResult result);
}
