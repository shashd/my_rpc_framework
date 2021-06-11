package github.zzz.rpc.core.provider;

/**
 * 保存和提供服务实例对象
 */
public interface ServiceProvider {

    /**
     * 向本地进行服务的注册
     * @param service 服务实体
     * @param serviceName 服务名称
     * @param <T> 泛型
     */
    <T> void addServiceProvider(T service, String serviceName);

    /**
     * 从本地的容易中根服务名得到对应的实例
     * @param serviceName 服务名称
     * @return Object服务实例
     */
    Object getServiceProvider(String serviceName);
}
