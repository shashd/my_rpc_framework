package github.zzz.rpc.core.remoting.transport.socket.client;


import github.zzz.rpc.core.remoting.dto.RpcRequest;
import github.zzz.rpc.core.remoting.dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 实现RPC客户端动态代理
 * 此处用的是JDK动态代理
 * @author zzz
 */
public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private String host;
    private int port;


    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 使用getProxy生成代理对象
     * 通过通过 Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
     * 方法创建代理对象
     * @param clazz 对象类型
     * @param <T> 类
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 实现动态代理的处理逻辑，指明代理对象的方法被调用时的动作
     * @param proxy 动态生成的代理类
     * @param method 与代理类对象调用的方法相对应
     * @param args 当前 method 方法的参数
     * @return Object
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("Used method: {}#{}",method.getDeclaringClass().getName(),method.getName());

        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),method.getDeclaringClass().getName(),
                method.getName(),args,method.getParameterTypes(),false);
        SocketClient socketClient = new SocketClient();
        return ((RpcResponse)socketClient.sendRequest(rpcRequest,host,port)).getData();
    }
}
