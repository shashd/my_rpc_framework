package github.zzz.rpc.core.remoting.transport.socket.server;

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

    /**
     * 注册完一个服务后服务器立刻开始监听
     * 通过WorkerThread执行具体的监听逻辑
     * @param service 注册服务
     * @param port 端口号
     */
    public void register(Object service, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is starting...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                logger.info("Client connected, ip: " + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (Exception e) {
            logger.error("Error happens when running: ", e);
        }
    }


}
