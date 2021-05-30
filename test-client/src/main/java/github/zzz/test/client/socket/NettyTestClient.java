package github.zzz.test.client.socket;

import com.google.common.annotations.VisibleForTesting;
import github.zzz.rpc.api.Hello;
import github.zzz.rpc.api.HelloService;
import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.core.remoting.transport.netty.client.NettyClient;
import github.zzz.rpc.core.remoting.transport.socket.client.RpcClientProxy;

/**
 * netty客户端测试
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient("localhost",9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        // 使用的服务通过代理执行
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        Hello hello = new Hello(12,"This is a test");
        String res = helloService.sayHello(hello);
        System.out.println(res);
    }
}
