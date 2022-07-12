package cn.fan.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
@Data
@Entity
public class Song extends BaseModel {
    @Id
    private long id;

    @Column
    private String mid;

    @Column
    private String name;

    @Column
    private String title;

    @Column
    private String subTitle;
    @Column
    private boolean vip;
    /**
     * 唱片公司
     */
    @Column
    private int companyId;

    @Transient
    private Company company;
    /**
     * 歌曲流派
     */
    @Column
    private int genreId;

    @Transient
    private Genre genre;
    /**
     * 歌曲语种
     */
    @Transient
    private Language language;

    @Column
    private int languageId;

    /**
     * 发布时间
     */
    @Column
    private Date publishTime;

    @Column
    private long mvId;

    @Transient
    private Mv mv;

    @Transient
    private Album album;

    @Column
    private long albumId;

    @Transient
    List<Singer> singers;


}
