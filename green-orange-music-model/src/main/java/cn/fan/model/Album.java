package cn.fan.model;

import lombok.Data;

import java.net.DatagramSocket;
import java.util.Date;
import java.util.List;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
@Data
public class Album {
    private long id;
    private String mid;
    private String name;
    private String tranName;
    private String desc;
    private Date publishTime;
    private int genreId;
    private Genre genre;
    private int languageId;
    private Language language;
    private int companyId;
    private Company company;
    List<Singer> singers;
}
