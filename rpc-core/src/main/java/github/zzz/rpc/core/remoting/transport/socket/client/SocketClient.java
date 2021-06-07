package github.zzz.rpc.core.remoting.transport.socket.client;

import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.common.entity.RpcRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 实现client通信的类
 * @author zzz
 */
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final String host;
    private final int port;

    public SocketClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    /**
     * 发送一个RpcRequest对象，并且接受返回的RpcResponse对象
     * @param rpcRequest 发送请求的对象
     * @return 读取到的对象
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest){
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
