package cn.fan.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
@Data
public class Mv {
    private long id;
    private String mid;
    private String desc;
    private String name;
    private long cloudPlayCount;
    private Date publishDate;
    private List<Singer> singers;
}
