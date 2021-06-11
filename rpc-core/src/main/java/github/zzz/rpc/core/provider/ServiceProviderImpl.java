package github.zzz.rpc.core.provider;

import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认实现的服务注册表，保存服务端本地服务
 * 就是一开始默认实现的服务注册器的简化版本
 */
public class ServiceProviderImpl implements ServiceProvider{

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    /**
     * 服务名和对象存储的容器
     * static保证全局唯一的注册信息
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 已经注册的服务名容器
     * static保证全局唯一的注册信息
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * 向本地进行服务的注册
     * @param service 服务实体
     * @param serviceName 服务名称
     * @param <T> 泛型
     */
    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if (registeredService.contains(serviceName)){
            return ;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName,service);
        logger.info("To the interface : {} , register the service : {}",service.getClass().getInterfaces(),serviceName);
    }

    /**
     * 从本地的容易中根服务名得到对应的实例
     * @param serviceName 服务名称
     * @return Object服务实例
     */
    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
