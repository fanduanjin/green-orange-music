import cn.fan.model.Singer;
import cn.fan.penguin.debug.core.http.DebugResult;
import cn.fan.penguin.debug.core.param.PenguinRequestParameterCreator;
import cn.fan.penguin.debug.core.request.SingerTotalRequest;
import cn.fan.penguin.debug.request.SingerInfoRequestImpl;
import cn.fan.penguin.debug.request.SingerListRequestImpl;
import cn.hutool.core.util.PageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/6/11
 * @Created by fanduanjin
 */
public class SingerInfoRequestTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final PenguinRequestParameterCreator penguinRequestParameterCreator =
            new PenguinRequestParameterCreator(objectMapper);
    private static final SingerTotalRequest SINGER_TOTAL_REQUEST =
            new SingerTotalRequest(penguinRequestParameterCreator);
    private static final SingerInfoRequestImpl singerInfoRequest =
            new SingerInfoRequestImpl(penguinRequestParameterCreator);

    private static final SingerListRequestImpl singerListRequest =
            new SingerListRequestImpl(penguinRequestParameterCreator);


    @Test
    public void singerInfoTest() {
        DebugResult<Integer> totalResult = SINGER_TOTAL_REQUEST.getSingerTotal();
        if (!totalResult.isSuccess()) {
            System.out.println("获取singer总数失败 : " + totalResult.getMessage());
        }
        int singerTotal = totalResult.getData();
        int pageTotal = PageUtil.totalPage(singerTotal, SingerListRequestImpl.SIN_OFFSET);
        CountDownLatch countDownLatch=new CountDownLatch(pageTotal);
        for (int pageIndex = 1; pageIndex <= pageTotal; pageIndex++) {
            int finalPageIndex=pageIndex;
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    DebugResult<List<Singer>> singerListResult= singerListRequest.getSingerList(finalPageIndex);
                    if(!singerListResult.isSuccess()){
                        System.out.println(Thread.currentThread().getId()+" : "+singerListResult.getMessage());
                        countDownLatch.countDown();
                        return;
                    }
                    for(Singer singer:singerListResult.getData()){
                        DebugResult<List<Singer>> singerInfoResult=
                                singerInfoRequest.getSingerInfoResult(singer.getMid());
                        if(!singerInfoResult.isSuccess()){
                            System.out.println("singerInfoResult : "+singerInfoResult.getMessage());
                            continue;
                        }
                        ShareConst.incNum();
                    }
                countDownLatch.countDown();
                }
            },ShareConst.executor);

        }
        try {
            countDownLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("总数量 : "+singerTotal+ " 成功数量 : "+ShareConst.getIncNum());
    }



}
