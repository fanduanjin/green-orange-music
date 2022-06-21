package cn.fan.penguin.debug.core.model;

import lombok.Data;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/21
 * @Created by fanduanjin
 */
@Data
public class MvUrlInfo {

    private String mid;
    /**
     * 标清
     */
    private String p360;
    /**
     * 高清
     */
    private String p480;
    /**
     * 超清
     */
    private String p720;
    /**
     * 全超清
     */
    private String p1080;

}
