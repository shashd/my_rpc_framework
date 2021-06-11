package github.zzz.rpc.core.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.common.utils.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 负责服务发现的部分
 * 其实就是搜索服务名，返回对应的地址而已
 */
public class NacosServiceDiscovery implements ServiceDiscovery{
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    /**
     * 根据服务名称从注册中心得到对应的地址
     * @param serviceName 服务名称
     * @return InetSocketAddress对象
     */
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            if (instances.size() == 0){
                logger.info("SERVICE_NOT_FOUND");
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e){
            logger.info("Error happens when connecting the Nacos",e);
        }
        return null;
    }
}
