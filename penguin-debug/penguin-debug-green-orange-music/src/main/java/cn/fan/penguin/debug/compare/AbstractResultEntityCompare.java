package cn.fan.penguin.debug.compare;

import cn.fan.model.Song;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/12
 * @Created by fanduanjin
 */
public abstract class AbstractResultEntityCompare<T> {

    /**
     * 判断是否用于当前比较器
     *
     * @param clazz
     * @return
     */
    public abstract boolean support(Class clazz);

    /**
     * 比较两个对象是否相等
     *
     * @param t1
     * @param t2
     * @return
     */
    protected abstract boolean compare(T t1, T t2);

    /**
     * @param t1
     * @param t2
     * @return
     */


    public boolean compareEntity(T t1, T t2) {
        if (t1 == t2) {
            return true;
        }
        if (t1 == null || t2 == null) {
            //其中一个为null return false;
            return false;
        }
        if (!support(t2.getClass())) {
            //排除不是当前比较器处理类型
            return false;
        }

        return compare(t1, t2);
    }
}
