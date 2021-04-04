package github.zzz.rpc.core.remoting.transport.socket.server;

import github.zzz.rpc.core.handler.RequestHandler;
import github.zzz.rpc.core.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 实现rpc中的server
 * 对结构进行了改动
 * @author zzz
 */
public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);


    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;

    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private final RequestHandler requestHandler;

    /**
     * 初始化线程池，注册服务等内容
     */
    public RpcServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,
                workingQueue,threadFactory);
        this.requestHandler = new RequestHandler();

    }

    /**
     * 注册完一个服务后服务器立刻开始监听
     * 通过WorkerThread执行具体的监听逻辑
     */
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is starting...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                logger.info("Client connected, ip: " + socket.getInetAddress());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (Exception e) {
            logger.error("Error happens when running: ", e);
        }
    }



}
