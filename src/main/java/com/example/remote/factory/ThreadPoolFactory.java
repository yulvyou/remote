package com.example.remote.factory;

/**
 * 线程池单例
 */
public class ThreadPoolFactory {
    //定义一个普通的线程池
    static ThreadPoolProxy mNormalPool;
    //定义一个下载的线程池
    static ThreadPoolProxy mDownLoadPool;

    /**
     * 得到一个普通的线程池
     */
    public static ThreadPoolProxy getNormalPool()
    {
        if(mNormalPool==null)
        {
            synchronized (ThreadPoolProxy.class)
            {
                if(mNormalPool==null)
                {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }//synchronized
        }//if
        return mNormalPool;
    }//getNormalPool
}
