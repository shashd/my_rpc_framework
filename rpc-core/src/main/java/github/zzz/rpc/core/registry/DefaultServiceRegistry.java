package github.zzz.rpc.core.registry;

import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现服务注册接口的具体逻辑
 * Set和Map的操作
 * @author zzz
 */
public class DefaultServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    /**
     * 服务名和对象存储的容器
     */
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 已经注册的服务名容器
     */
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    /**
     * 将服务对象存储到Map中
     * @param service 服务对象
     * @param <T> 存储对象
     */
    @Override
    public synchronized <T> void register(T service){
        // 1. 存储到set，默认采用这个对象实现的接口的完整类名作为服务名
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)){
            return ;
        }
        registeredService.add(serviceName);

        // 2. 存储到Map
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> cls: interfaces){
            serviceMap.put(cls.getCanonicalName(), service);
        }
        logger.info("For interface: {}, register service: {}", interfaces, serviceName);
    }


    /**
     * 从Map中获取到服务对象
     * @param serviceName 服务名称
     * @return 服务对象
     */
    @Override
    public synchronized Object getService(String serviceName){
        Object service = serviceMap.get(serviceName);
        if (service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }

}
