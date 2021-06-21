package github.zzz.rpc.common.utils;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

import com.alibaba.nacos.api.naming.pojo.Instance;
import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;

public class NacosUtil{

    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);

    private static final String SERVICE_ADDRESS = "127.0.0.1:8848";
    private static NamingService namingService;
    private static final Set<String> serviceNames;
    private static InetSocketAddress inetSocketAddress;

    static {
        serviceNames = new HashSet<String>();
        namingService = getNacosNamingService();
    }

    /**
     * 使用NamingFactory创建namingService
     * @return namingService
     */
    public static NamingService getNacosNamingService(){
        try {
            return NamingFactory.createNamingService(SERVICE_ADDRESS);
        } catch (NacosException e){
            logger.info("Error happens when connecting the Nacos in Nacos Util");
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    /**
     * 本地的set和namingService中一起注册服务
     * @param serviceName 服务名
     * @param address 实现IP套接字地址（IP地址+端口号）
     */
    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException{
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.inetSocketAddress = address;
        serviceNames.add(serviceName);
    }

    /**
     * 查找服务名，得到所有的服务名所对应的地址对象
     * @param serviceName 服务明
     * @return List<Integer>
     * @throws NacosException Nacos异常
     */
    public static List<Instance> getAllInstance(String serviceName) throws NacosException{
        return namingService.getAllInstances(serviceName);
    }

    /**
     * 清除服务
     * 通过当前的host和port，对set进行遍历，得到其下所有的serviceName，然后将所有的都清除
     */
    public static void clearRegistry(){
        if (!serviceNames.isEmpty() && inetSocketAddress != null){
            String host = inetSocketAddress.getHostName();
            int port = inetSocketAddress.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while (iterator.hasNext()){
                String serviceName = iterator.next();
                try {
                    namingService.deregisterInstance(serviceName,host,port);
                } catch (NacosException e){
                    logger.info("deregister the service fails : {}", serviceName,e);
                }
            }
        }
    }



}
