package github.zzz.rpc.core.remoting;

import github.zzz.rpc.core.remoting.transport.socket.util.ObjectWriter;
import github.zzz.rpc.core.serializer.CommonSerializer;

/**
 * 服务器端抽象接口
 *
 */
public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 服务器端启动监听和处理的逻辑
     * @param port 端口号
     */
    void start(int port);


    /**
     * 设置序列化器
     * @param serializer 序列化实例
     */
    void setSerializer(CommonSerializer serializer);


    /**
     * 向Nacos注册服务
     * @param service 服务对象
     * @param serviceClass 服务的类
     * @param <T>
     */
    <T> void publishService(Object service, Class<T> serviceClass);

}
