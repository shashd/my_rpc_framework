package github.zzz.test.server.socket;

import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.remoting.transport.socket.server.RpcServer;
import github.zzz.test.server.impl.HelloServiceImpl;

/**
 * 测试socket连接
 * @author zzz
 */
public class SocketTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService,9000);

    }
}
