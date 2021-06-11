package github.zzz.rpc.core.registry;

import java.net.InetSocketAddress;

/**
 * Nacos用的服务注册接口
 */
public interface ServiceRegistry {

    /**
     * 注册服务信息（名称和地址）到注册中心
     * @param serviceName 服务名称
     * @param inetSocketAddress 实现IP套接字地址（IP地址+端口号）
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

}
