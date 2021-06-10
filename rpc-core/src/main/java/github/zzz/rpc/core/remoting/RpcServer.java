package github.zzz.rpc.core.remoting;

import github.zzz.rpc.core.serializer.CommonSerializer;

/**
 * 服务器端抽象接口
 *
 */
public interface RpcServer {
    /**
     * 服务器端启动监听和处理的逻辑
     * @param port 端口号
     */
    void start(int port);


    void setSerializer(CommonSerializer serializer);
}
