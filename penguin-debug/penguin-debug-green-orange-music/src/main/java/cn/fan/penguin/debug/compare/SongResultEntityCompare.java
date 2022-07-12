package cn.fan.penguin.debug.compare;

import cn.fan.model.Song;

import java.util.Date;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/12
 * @Created by fanduanjin
 */
public class SongResultEntityCompare extends AbstractResultEntityCompare<Song> {
    @Override
    public boolean support(Class clazz) {
        return clazz == Song.class;
    }

    @Override
    public boolean compare(Song t1, Song t2) {
        if (t1.getId() != t1.getId()) {
            return false;
        }
        //mid
        String tmpString = t1.getMid() == null ? "" : t1.getMid();
        if (!tmpString.equals(t1.getMid())) {
            return false;
        }
        //name
        tmpString = t1.getName() == null ? "" : t1.getName();
        if (!tmpString.equals(t1.getName())) {
            return false;
        }
        //title
        tmpString = t1.getTitle() == null ? "" : t1.getTitle();
        if (!tmpString.equals(t1.getTitle())) {
            return false;
        }
        //subTitle
        tmpString = t1.getSubTitle() == null ? "" : t1.getSubTitle();
        if (!tmpString.equals(t1.getSubTitle())) {
            return false;
        }
        //publishTime;
        Date tmpDate = t1.getPublishTime() == null ? new Date() : t1.getPublishTime();
        if (!tmpDate.equals(t1.getPublishTime())) {
            return false;
        }
        //mvId
        if (t1.getMvId() != t1.getMvId()) {
            return false;
        }
        //albumId
        if (t1.getAlbumId() != t2.getAlbumId()) {
            return false;
        }
        return true;
    }
}
