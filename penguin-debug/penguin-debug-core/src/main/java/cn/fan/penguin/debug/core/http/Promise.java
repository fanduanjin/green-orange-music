package cn.fan.penguin.debug.core.http;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/22
 * @Created by fanduanjin
 */
public class Promise<T> {

    private Consumer<T> success;
    private Consumer<String> fail;


    public Promise fail(Consumer<String> consumer) {
        this.fail = consumer;
        return this;
    }

    public Promise success(Consumer<T> consumer) {
        this.success = consumer;
        return this;
    }

    public boolean end(DebugResult<T> result) {
        if (result.isSuccess()) {
            this.success.accept(result.getData());
            return true;
        } else {
            this.fail.accept(result.getMessage());
            return false;
        }
    }

}
