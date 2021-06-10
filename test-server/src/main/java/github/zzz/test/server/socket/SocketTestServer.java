package github.zzz.test.server.socket;

import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.registry.DefaultServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.transport.socket.server.SocketServer;
import github.zzz.rpc.core.serializer.KryoSerializer;
import github.zzz.test.server.impl.HelloServiceImpl;

/**
 * 测试socket连接
 * todo: 文档中有些打日志的姿势似乎是有问题的
 *
 */
public class SocketTestServer {
    public static void main(String[] args) {
        // 1. register the service
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        // 2. start request handler
        // 暴露的是单个的服务
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.start(9000);

    }
}
