package github.zzz.test.server.socket;

import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.registry.DefaultServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.transport.socket.server.SocketServer;
import github.zzz.test.server.impl.HelloServiceImpl;

/**
 * 测试socket连接
 * @author zzz
 */
public class SocketTestServer {

    public static void main(String[] args) {
        // 1. register the service
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        // 2. start request handler
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);

    }
}
