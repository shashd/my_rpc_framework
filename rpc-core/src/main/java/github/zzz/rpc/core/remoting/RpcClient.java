package github.zzz.rpc.core.remoting;

import github.zzz.rpc.core.remoting.dto.RpcRequest;
import github.zzz.rpc.core.serializer.CommonSerializer;

/**
 * 客户端抽象接口
 * @author zzz
 */
public interface RpcClient {

    int default_serializer = CommonSerializer.DEFAULT_SERIALIZER;

    /**
     * 发送请求
     * @param rpcRequest
     * @param host
     * @param port
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest, String host, int port);
}
