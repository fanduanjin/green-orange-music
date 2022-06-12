package cn.fan.model;

import cn.fan.model.$enum.SingerType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/8
 * @Created by fanduanjin
 */
@Data
public class Singer {

    private long id;
    private String mid;
    private String name;
    private SingerType type;
    private int area;
    private String desc;
    private int genre;
    private String foreignName;
    private Date birthday;
    /**
     * 成就
     */
    private String wiki;
    private String pic;
    private List<Singer> singers;

}
