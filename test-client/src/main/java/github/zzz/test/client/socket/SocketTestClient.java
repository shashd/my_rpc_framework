package github.zzz.test.client.socket;

import github.zzz.rpc.api.Hello;
import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.remoting.transport.socket.client.RpcClientProxy;

/**
 * socket客户端测试
 * @author zzz
 */
public class SocketTestClient {

    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy("localhost",9000);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello(12,"This is a message.");
        String res = helloService.sayHello(hello);
        System.out.println(res);
    }
}
