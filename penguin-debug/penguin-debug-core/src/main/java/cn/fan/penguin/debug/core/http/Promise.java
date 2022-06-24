package cn.fan.penguin.debug.core.http;

import java.util.Scanner;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/22
 * @Created by fanduanjin
 */
public class Promise<T> {

    Successful<T> successful;
    Failed failed;

    public  Promise success(Successful<T> successful){
        this.successful= successful;
        return this;
    }

    public Promise failed(Failed failed){
        this.failed=failed;
        return this;
    }

    public void when(DebugResult<T> debugResult){
        if(debugResult.isSuccess()){
            successful.success(debugResult.getData());
        }else{
            failed.failed(debugResult.getMessage());
        }
    }


}
