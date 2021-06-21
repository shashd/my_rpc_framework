package github.zzz.rpc.common.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建线程池的工具
 * 使用guava中的ThreadFactoryBuilder来帮助创建一个线程池
 *
 */
public class ThreadPoolFactory {

    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);
    private static Map<String,ExecutorService> threadPoolsMap = new ConcurrentHashMap<String,ExecutorService>();

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

    public static ExecutorService createDefaultThreadPool(final String threadNamePrefix, final Boolean daemon){
        // 通过computeIfAbsent构建本地缓存
        // 对该数值进行重新计算，如果不存在就加入到hashmap中，并且有数值返回
        ExecutorService pool = threadPoolsMap.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix,daemon));
        // 如果已经关闭就可以创建一个新的
        if (pool.isShutdown() || pool.isTerminated()){
            threadPoolsMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix,daemon);
            threadPoolsMap.put(threadNamePrefix,pool);
        }
        return pool;
    }

    public static void shutDownAll(){
        logger.info("closing all the threads");
        threadPoolsMap.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("Close the thread pool [{}] [{}]", entry.getKey(),executorService.isTerminated());
            try {
                // 阻塞直到所有任务在关闭请求后完成执行，或发生超时，或当前线程被中断，以先发生者为准
                executorService.awaitTermination(10,TimeUnit.SECONDS);
            } catch (InterruptedException e){
                logger.error("Fail to close the thread pool");
                // 马上关闭线程池
                executorService.shutdownNow();
            }
        });
    }

    /**
     * 创建线程池，需要线程工厂和之前表示的所有参数
     * @param threadNamePrefix 线程前缀
     * @param daemon 是否设置守护线程
     * @return ThreadPoolExecutor
     */
    public static ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon) {
        // 有界队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix,daemon);
        return new ThreadPoolExecutor(COOL_POLE_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.MINUTES,workQueue,threadFactory);
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
