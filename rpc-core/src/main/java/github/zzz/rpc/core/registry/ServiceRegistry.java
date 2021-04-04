package github.zzz.rpc.core.registry;

/**
 * 服务注册接口
 * @author zzz
 */
public interface ServiceRegistry {

    /**
     * 注册服务信息
     * @param service 服务对象
     * @param <T> 对象
     */
    <T> void register(T service);

    /**
     * 根据服务名获取服务细心
     * @param serviceName 服务名称
     * @return 服务信息
     */
    Object getService(String serviceName);
}
