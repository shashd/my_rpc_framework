package github.zzz.rpc.core.remoting;

import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.core.serializer.CommonSerializer;

/**
 * 客户端抽象接口, 这样可以提供不同的实现方案
 * BIO以及NIO的实现方案
 *
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 发送请求
     * @param rpcRequest
     * @return rpcResponse/null
     */
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);

}
