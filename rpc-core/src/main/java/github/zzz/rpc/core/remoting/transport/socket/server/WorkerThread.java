package github.zzz.rpc.core.remoting.transport.socket.server;

import github.zzz.rpc.core.remoting.dto.RpcRequest;
import github.zzz.rpc.core.remoting.dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 自定义workerThread
 * @author zzz
 */
public class WorkerThread implements Runnable{


    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    /**
     * socket
     */
    private Socket socket;

    /**
     * service
     */
    private Object service;

    public WorkerThread(Socket socket, Object service){
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run(){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object returnObject = method.invoke(service, rpcRequest.getParameters());

            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (Exception e) {
            logger.error("Error happens when running：", e);
        }

    }
}
