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
public class Song {
    private long id;
    private String mid;
    private String name;
    private String title;
    private String subTitle;
    /**
     * 唱片公司
     */
    private int companyId;
    private Company company;
    /**
     * 歌曲流派
     */
    private int genreId;
    private Genre genre;
    /**
     * 歌曲语种
     */
    private Language language;
    private int languageId;

    /**
     * 发布时间
     */
    private Date publishTime;

    private long mvId;
    private Mv mv;

    private Album album;
    private long albumId;

    List<Singer> singers;



}
