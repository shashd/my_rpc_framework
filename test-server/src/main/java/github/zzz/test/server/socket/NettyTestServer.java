package github.zzz.test.server.socket;

import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.registry.DefaultServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.transport.netty.server.NettyServer;
import github.zzz.rpc.core.serializer.KryoSerializer;
import github.zzz.test.server.impl.HelloServiceImpl;

/**
 * netty服务端测试
 */
public class NettyTestServer {

    public static void main(String[] args) {
        // 创建nettyClient
        NettyServer nettyServer = new NettyServer();
        // 服务注册
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        HelloService helloService = new HelloServiceImpl();
        serviceRegistry.register(helloService);
        // 开始运行
        nettyServer.setSerializer(new KryoSerializer());
        nettyServer.start(9999);
    }
}
