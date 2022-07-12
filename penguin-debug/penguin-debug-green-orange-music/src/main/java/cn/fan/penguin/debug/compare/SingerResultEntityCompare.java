package cn.fan.penguin.debug.compare;

import cn.fan.model.Singer;

import java.lang.invoke.MutableCallSite;
import java.util.Date;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/12
 * @Created by fanduanjin
 */
public class SingerResultEntityCompare extends AbstractResultEntityCompare<Singer> {
    @Override
    public boolean support(Class clazz) {
        return clazz == Singer.class;
    }

    @Override
    protected boolean compare(Singer t1, Singer t2) {
        if (t1.getId() != t2.getId()) {
            return false;
        }
        //mid
        String tmpString = t1.getMid() == null ? "" : t1.getMid();
        if (!tmpString.equals(t2.getMid())) {
            return false;
        }
        //name
        tmpString = t1.getName() == null ? "" : t1.getName();
        if (!tmpString.equals(t2.getName())) {
            return false;
        }
        //birthday
        Date tmpDate = t1.getBirthday() == null ? new Date() : t1.getBirthday();
        if (!tmpDate.equals(t2.getBirthday())) {
            return false;
        }
        //desc
        tmpString = t1.getDesc() == null ? "" : t1.getDesc();
        if (null != t2.getDesc() && tmpString.length() != t2.getDesc().length()) {
            return false;
        } else if (!tmpString.equals(t2.getDesc())) {
            return false;
        }
        //foreignName
        tmpString = t1.getForeignName() == null ? "" : t1.getForeignName();
        if (!tmpString.equals(t2.getForeignName())) {
            return false;
        }
        //pic
        tmpString = t1.getPic() == null ? "" : t1.getPic();
        if (!tmpString.equals(t2.getPic())) {
            return false;
        }
        //wiki
        tmpString = t1.getWiki() == null ? "" : t1.getWiki();
        if (t2.getWiki() != null && tmpString.length() != t2.getWiki().length()) {
            return false;
        } else if (!tmpString.equals(t2.getWiki())) {
            return false;
        }
        return true;
    }

}
