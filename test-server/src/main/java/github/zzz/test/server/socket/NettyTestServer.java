package github.zzz.test.server.socket;

import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.registry.NacosServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.transport.netty.server.NettyServer;
import github.zzz.rpc.core.serializer.KryoSerializer;
import github.zzz.test.server.impl.HelloServiceImpl;

/**
 * netty服务端测试
 */
public class NettyTestServer {

    public static void main(String[] args) {
        // 声明提供的服务
        HelloService helloService = new HelloServiceImpl();
        // 创建nettyClient
        NettyServer nettyServer = new NettyServer("127.0.0.1", 9999);
        // 设置序列化方式
        nettyServer.setSerializer(new KryoSerializer());
        // 推送服务到Nacos上
        nettyServer.publishService(helloService,HelloService.class);
    }
}
