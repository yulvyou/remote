package com.example.remote;

import com.example.remote.factory.ThreadPoolFactory;
import com.example.remote.utils.ThreadPoolUtils;
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
//        clint.isFirstLunch();
    }

    @Test
    public void threadPoolTest(){
        ExecutorService executor = ThreadPoolFactory.getNormalPool();
        for(int i = 0; i<5;i++){
            Task task1 = new Task();
            Future<Boolean> f1 = executor.submit(task1);
            ThreadPoolUtils.monitorTask(f1,2);
            int j = ((ThreadPoolExecutor)executor).getActiveCount();
            log.info(j + "");
        }
    }

}