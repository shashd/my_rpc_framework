package github.zzz.rpc.core.remoting.transport.socket.server;

import github.zzz.rpc.core.handler.RequestHandler;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 处理服务注册的线程
 * 应该和以前的workerThread大同小异
 * @author zzz
 */
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry){
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * 将workerThread中的逻辑进行修改，并且注册下服务
     */
    @Override
    public void run(){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 1. get rpcRequest
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 2. get service object by interface name
            String interfaceName  = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            // 3. get response object
            objectOutputStream.writeObject(RpcResponse.success(result,rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (Exception e) {
            logger.error("Error happens when running：", e);
        }
    }
}
