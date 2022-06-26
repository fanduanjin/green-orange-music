package cn.fan.penguin.debug.core.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/22
 * @Created by fanduanjin
 */

@FunctionalInterface
public interface Successful<T> {

    /**
     *
     */
    <T> void success(T t);
}
