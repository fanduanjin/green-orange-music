package cn.fan.model;


import cn.fan.model.$enum.SingerType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/8
 * @Created by fanduanjin
 */
@Data
@Entity
public class Singer  extends BaseModel{

    @Id
    private long id;

    @Column(nullable = false)
    private String mid;
    @Column(nullable = false)
    private String name;

    @Column
    private SingerType type;



    @Column(name = "`desc`",columnDefinition = "text")
    private String desc ;

    @Column
    private String foreignName;

    @Column
    private Date birthday;
    /**
     * 成就
     */
    @Column(columnDefinition = "text")
    private String wiki;

    @Column
    private String pic;

    @Transient
    private List<Singer> singers;

}
