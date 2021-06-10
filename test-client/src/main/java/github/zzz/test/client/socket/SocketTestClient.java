package github.zzz.test.client.socket;

import github.zzz.rpc.api.Hello;
import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.core.remoting.RpcClientProxy;
import github.zzz.rpc.core.remoting.transport.socket.client.SocketClient;
import github.zzz.rpc.core.serializer.KryoSerializer;

/**
 * socket客户端测试
 *
 */
public class SocketTestClient {

    public static void main(String[] args) {
        RpcClient client = new SocketClient("localhost",9000);
        client.setSerializer(new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello(12,"This is a message.");
        String res = helloService.sayHello(hello);
        System.out.println(res);
    }
}
