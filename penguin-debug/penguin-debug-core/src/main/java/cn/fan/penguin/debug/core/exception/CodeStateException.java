package cn.fan.penguin.debug.core.exception;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class CodeStateException extends RuntimeException {
    public CodeStateException(String url){
        super("爬取数据响应code有误 : "+url);
    }
}
