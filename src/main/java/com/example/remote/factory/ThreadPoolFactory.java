package com.example.remote.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池单例
 */
public class ThreadPoolFactory {
    //定义一个普通的线程池
    static ExecutorService mNormalPool;
    /**
     * 得到一个普通的线程池
     */
    public static ExecutorService getNormalPool()
    {
        if(mNormalPool==null)
        {
            synchronized (ExecutorService.class)
            {
                if(mNormalPool==null)
                {
                    mNormalPool = Executors.newFixedThreadPool(5);
                }
            }//synchronized
        }//if
        return mNormalPool;
    }//getNormalPool
}
