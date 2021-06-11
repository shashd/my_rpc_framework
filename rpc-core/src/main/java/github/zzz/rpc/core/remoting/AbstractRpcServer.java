package github.zzz.rpc.core.remoting;

import github.zzz.rpc.core.provider.ServiceProvider;
import github.zzz.rpc.core.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象类必须被继承才可以被使用
 * 可以用来定制化原始的RpcServer
 */
public abstract class AbstractRpcServer implements RpcServer {

    protected Logger logger = LoggerFactory.getLogger(AbstractRpcServer.class);

    protected String host;
    protected int port;

    protected ServiceRegistry serviceRegistry;
    protected ServiceProvider serviceProvider;

    // 扫描服务的功能的实现?



}
