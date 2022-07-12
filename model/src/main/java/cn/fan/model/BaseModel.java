package cn.fan.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/25
 * @Created by fanduanjin
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel implements Serializable {
    /**
     * 创建时间
     */
    @Column
    @CreatedDate
    private Date createDate;

    /**
     * 修改时间
     */
    @Column
    @LastModifiedDate
    private Date updateDate;
}
