package github.zzz.rpc.core.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.common.utils.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * 实现服务中心注册接口 - 初始的版本
 */
public class NacosServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);


    /**
     * 通过namingService来注册服务
     * @param serviceName 服务名称
     * @param inetSocketAddress 实现IP套接字地址（IP地址+端口号）
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress){
        try {
            NacosUtil.registerService(serviceName,inetSocketAddress);
        } catch (NacosException e){
            logger.info("Error happens when connecting the Nacos");
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

}
