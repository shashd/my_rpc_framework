package github.zzz.rpc.core.loadbalancer;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡的策略
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
