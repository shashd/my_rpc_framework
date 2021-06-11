package github.zzz.test.server.socket;

import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.registry.NacosServiceRegistry;
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
        // 1. 声明服务
        HelloService helloService = new HelloServiceImpl();
        // 2. start request handler
        SocketServer socketServer = new SocketServer("127.0.0.1",9998);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.publishService(helloService,HelloService.class);

    }
}
