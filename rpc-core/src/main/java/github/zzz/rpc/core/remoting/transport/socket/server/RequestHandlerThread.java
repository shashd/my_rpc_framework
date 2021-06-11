package github.zzz.rpc.core.remoting.transport.socket.server;

import github.zzz.rpc.core.handler.RequestHandler;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import github.zzz.rpc.core.remoting.transport.socket.util.ObjectReader;
import github.zzz.rpc.core.remoting.transport.socket.util.ObjectWriter;
import github.zzz.rpc.core.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 处理服务注册的线程
 * 应该和以前的workerThread大同小异
 *
 */
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;


    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer){
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    /**
     * 将workerThread中的逻辑进行修改，并且注册下服务
     */
    @Override
    public void run(){
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            // 1. get rpcRequest
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            // 3. get response object
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream,response,serializer);
        } catch (Exception e) {
            logger.error("Error happens when running：", e);
        }
    }
}
