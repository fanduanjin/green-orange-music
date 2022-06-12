package cn.fan.penguin.debug.core.param;

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
public class PenguinRequestGroupParameter {

    private String module;
    private String method;
    private Object param;

    public PenguinRequestGroupParameter(String module,String method,Object param){
        this.method=method;
        this.module=module;
        this.param=param;
    }
}
