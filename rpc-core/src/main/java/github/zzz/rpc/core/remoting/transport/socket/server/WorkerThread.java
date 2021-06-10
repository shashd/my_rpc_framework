package github.zzz.rpc.core.remoting.transport.socket.server;

import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 自定义workerThread，仅仅在用最简单的socket通信的时候使用
 *
 */
public class WorkerThread implements Runnable{


    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Socket socket;
    private Object service;

    public WorkerThread(Socket socket, Object service){
        this.socket = socket;
        this.service = service;
    }

    /**
     * 服务端接受RpcRequest处理之后返回RpcResponse
     */
    @Override
    public void run(){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 通过反射得到方法执行后的结果
            Object returnObject = method.invoke(service, rpcRequest.getParameters());

            objectOutputStream.writeObject(RpcResponse.success(returnObject,rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (Exception e) {
            logger.error("Error happens when running：", e);
        }

    }
}
