package github.zzz.rpc.core.transport.socket.server;

import github.zzz.rpc.core.transport.WorkerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 实现rpc中的server
 * @author zzz
 */
public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private final ExecutorService threadPool;

    /**
     * 初始化线程池
     */
    public RpcServer(){
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,
                workingQueue,threadFactory);

    }


}
