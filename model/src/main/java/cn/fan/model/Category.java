package cn.fan.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/7
 * @Created by fanduanjin
 */
@Data
@Entity
@Deprecated
public class Category extends BaseModel {
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column
    private int groupId;

}
