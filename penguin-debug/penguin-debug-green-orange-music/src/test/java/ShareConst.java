import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class ShareConst {

    public static final ExecutorService executor =
            Executors.newFixedThreadPool(80);

    private static int incNum=0;

    private static int totalNum=0;

    public static synchronized void incNum(){
        incNum++;
    }

    public static synchronized int getIncNum(){
        return incNum;
    }

    public static synchronized void totalNum(int total){
        totalNum+=total;
    }

    public static synchronized int getTotalNum(){
        return totalNum;
    }
}
