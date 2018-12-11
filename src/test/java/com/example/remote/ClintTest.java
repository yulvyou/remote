package com.example.remote;

import com.example.remote.factory.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ClintTest {

    static class Task implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            while (true){

                log.info("正在运行");
                try {
                    Thread.sleep(3000);
                }catch (InterruptedException e) {
                    log.info(" is interrupted when calculating, will stop...");
                    return false; // 注意这里如果不return的话，线程还会继续执行，所以任务超时后在这里处理结果然后返回
                }

                log.info("sleep结束");
                return true;
            }


        }
    }


    @Test
    public void isFirstLunch() {
        Clint clint = new Clint();
        clint.isFirstLunch();
    }

    @Test
    public void threadPoolTest(){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Task task1 = new Task();
        Future<Boolean> f1 = executor.submit(task1);
        try {
            if (f1.get(2, TimeUnit.SECONDS)) { // future将在2秒之后取结果
                log.info("one complete successfully");
            }
        } catch (InterruptedException e) {
            log.info("future在睡着时被打断");
            executor.shutdownNow();
        } catch (ExecutionException e) {
            log.info("future在尝试取得任务结果时出错");
            executor.shutdownNow();
        } catch (TimeoutException e) {
            log.info("future时间超时");
            f1.cancel(true);
            // executor.shutdownNow();
            // executor.shutdown();
        } finally {
//            executor.shutdownNow();
        }
        executor.shutdown();



    }

}