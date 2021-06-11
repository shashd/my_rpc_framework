package github.zzz.rpc.common.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 创建线程池的工具
 * 使用guava中的ThreadFactoryBuilder来帮助创建一个线程池
 *
 */
public class ThreadPoolFactory {

    // 那么是否奥机场什么类来实现这个线程池
    // 面试的问题，你一般怎么实现线程池

    private static final int COOL_POLE_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    ThreadPoolFactory(){}

    /**
     * 封装暴露工厂创建接口
     * @param threadNamePrefix 线程前缀
     * @return ExecutorService
     */
    public static ExecutorService createDefaultThreadFactory(String threadNamePrefix){
        return createDefaultThreadPool(threadNamePrefix,false);
    }

    /**
     * 创建线程池，需要线程工厂和之前表示的所有参数
     * @param threadNamePrefix 线程前缀
     * @param daemon 是否设置守护线程
     * @return ThreadPoolExecutor
     */
    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon) {
        // 有界队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix,daemon);
        return new ThreadPoolExecutor(COOL_POLE_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.MINUTES,workQueue);
    }

    /**
     * 创建线程工厂
     * @param threadNamePrefix 线程名前缀
     * @param daemon 是否创建守护线程
     * @return threadFactory
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon){
        if (threadNamePrefix != null){
            if (daemon != null){
                // 设置守护线程
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
            } else{
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }



}
