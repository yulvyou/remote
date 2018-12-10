package com.example.remote.factory;

import java.util.concurrent.*;

/**
 * 线程池
 */
public class ThreadPoolProxy {
    //定义线程池
    ThreadPoolExecutor mExcutor;//只创建一次

    int mMaximumPoolSize;
    int mCorePoolSize;
    long mKeepAliveTime;

    public ThreadPoolProxy(int maximumPoolSize, int corePoolSize,
                           long keepAliveTime) {
        super();
        this.mMaximumPoolSize = maximumPoolSize;
        this.mCorePoolSize = corePoolSize;
        this.mKeepAliveTime = keepAliveTime;
    }


    /**
     * 执行任务
     */
    public void execute(Runnable task)
    {
        //初始化线程池
        initThreadPoolExecutor();
        mExcutor.execute(task);
    }//execute


    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task)
    {
        return mExcutor.submit(task);
    }//submit

    /**
     * 移除任务
     */
    public void removeTask(Runnable task)
    {
        mExcutor.remove(task);
    }//removeTask


    /**
     * 初始化线程池
     */
    private ThreadPoolExecutor initThreadPoolExecutor()
    {
        // 线程池只创建一次（单例模式）
        if(mExcutor == null)//双重加锁检查，保证线程安全
        {
            synchronized (ThreadPoolExecutor.class)
            {
                if(mExcutor == null)
                {
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueu = new LinkedBlockingQueue<Runnable>();//无界队列
                    ThreadFactory threadFatory  = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();//丢弃任务并抛出异常

                    mExcutor = new ThreadPoolExecutor(
                            mCorePoolSize, //核心线程数
                            mMaximumPoolSize,// 最大线程数
                            mKeepAliveTime, //保持时间
                            unit, //保持时间对应的单位
                            workQueu,//缓存队列、阻塞队列
                            threadFatory,//线程工厂
                            handler//异常捕获
                    );
                }
            }//synchronized

        }//if
        return mExcutor;
    }//initThreadPoolExecutor
}
