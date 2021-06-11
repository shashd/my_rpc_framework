package github.zzz.rpc.core.remoting.transport.socket.client;

import github.zzz.rpc.common.entity.RpcResponse;
import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.common.utils.RpcMessageChecker;
import github.zzz.rpc.core.provider.ServiceProvider;
import github.zzz.rpc.core.registry.NacosServiceDiscovery;
import github.zzz.rpc.core.registry.ServiceDiscovery;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.core.remoting.transport.socket.util.ObjectReader;
import github.zzz.rpc.core.remoting.transport.socket.util.ObjectWriter;
import github.zzz.rpc.core.serializer.CommonSerializer;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 实现client通信的类
 * 添加Nacos之后直接从serviceDiscovery中获取地址和端口
 */
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private CommonSerializer serializer;
    private static ServiceDiscovery serviceDiscovery = new NacosServiceDiscovery();

    /**
     * 发送一个RpcRequest对象，并且接受返回的RpcResponse对象
     * @param rpcRequest 发送请求的对象
     * @return 读取到的对象
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest){
        if (serializer == null){
            logger.error("Did not set the serializer");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try(Socket socket = new Socket(inetSocketAddress.getHostName(),inetSocketAddress.getPort())){
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            // 序列化发送
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            // 得到反序列化后的结果
            RpcResponse rpcResponse= (RpcResponse)ObjectReader.readObject(inputStream);
            // 进行校验
            RpcMessageChecker.check(rpcRequest,rpcResponse);
            return rpcResponse.getData();
        }catch (Exception e){
            logger.info("Error happens when sending request: ",e);
            return null;
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
