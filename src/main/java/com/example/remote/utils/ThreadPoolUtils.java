package com.example.remote.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
@Slf4j
public class ThreadPoolUtils {

    public static void monitorTask(Future<Boolean> future,long timeout){
        try {
            if (future.get(timeout, TimeUnit.MINUTES)) { // future将在timeout分钟之后取结果
                log.info("执行命令完成");
            }
        } catch (InterruptedException e) {
            log.info("任务被打断");
            future.cancel(true);
        } catch (ExecutionException e) {
            log.error("任务在尝试取得任务结果时出错");
            future.cancel(true);
        } catch (TimeoutException e) {
            log.error("执行任务超时");
            future.cancel(true);
            // executor.shutdownNow();
            // executor.shutdown();
        }
    }
}
