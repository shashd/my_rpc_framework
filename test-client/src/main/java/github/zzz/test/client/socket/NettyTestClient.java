package github.zzz.test.client.socket;

import github.zzz.rpc.api.Hello;
import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.core.remoting.transport.netty.client.NettyClient;
import github.zzz.rpc.core.remoting.RpcClientProxy;
import github.zzz.rpc.core.serializer.KryoSerializer;

/**
 * netty客户端测试
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello(12,"This is a test");
        String res = helloService.sayHello(hello);
        System.out.println(res);
    }
}
