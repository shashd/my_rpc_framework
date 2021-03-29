package github.zzz.rpc.core.transport.socket.client;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.core.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 实现client通信的类
 * @author zzz
 */
public class RpcClient{

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    int default_serializer = CommonSerializer.DEFAULT_SERIALIZER;

    /**
     * 发送一个RpcRequest对象，并且接受返回的RpcResponse对象
     * @param rpcRequest 发送请求的对象
     * @return 读取到的对象
     */
    public Object sendRequest(RpcRequest rpcRequest, String host, int port){
        try(Socket socket = new Socket(host,port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        }catch (Exception e){
            logger.info("Error happens when sending request: ",e);
            return null;
        }
    }
}
