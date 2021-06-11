package github.zzz.rpc.core.registry;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    /**
     * 根据服务名称从注册中心得到对应的地址
     * @param serviceName 服务名称
     * @return InetSocketAddress
     */
    InetSocketAddress lookupService(String serviceName);


}
