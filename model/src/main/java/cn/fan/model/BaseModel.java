package cn.fan.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/25
 * @Created by fanduanjin
 */
public class BaseModel implements Serializable {
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;
}
