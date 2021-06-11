package github.zzz.rpc.core.remoting.transport.socket.server;

import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.core.handler.RequestHandler;
import github.zzz.rpc.core.provider.ServiceProvider;
import github.zzz.rpc.core.provider.ServiceProviderImpl;
import github.zzz.rpc.core.registry.NacosServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.RpcServer;
import github.zzz.rpc.core.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 实现rpc中的server
 * 添加了Nacos之后通过ServiceRegistry和ServiceProvider来注册服务和实例
 */
public class SocketServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);


    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;

    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    private final RequestHandler requestHandler;
    private CommonSerializer serializer;
    private String host;
    private int port;

    /**
     * 初始化线程池，注册服务等内容
     * 注册中心的类作为参数传入
     */
    public SocketServer(String host, int port){
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
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
    @Override
    public void start(int port) {
        if (serializer == null){
            logger.error("Did not set the serializer");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server is starting...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                logger.info("Client connected, ip: " + socket.getInetAddress());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (Exception e) {
            logger.error("Error happens when running: ", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (service == null){
            logger.info("publishService - service is not found");
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        String serviceName = serviceClass.getCanonicalName();
        serviceProvider.addServiceProvider(service,serviceName);
        serviceRegistry.register(serviceName,new InetSocketAddress(host,port));
        // 带来的问题是一个服务端只能注册一个服务，而不是多个，因为直接调用了start，需要进行修改
        start(port);

    }
}
