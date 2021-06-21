package github.zzz.rpc.core.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 随机算法
 */
public class RandomLoadBalancer implements LoadBalancer{

    @Override
    public Instance select(List<Instance> instances) {
        // 随机从List中随机取出一个内容就是了
        return instances.get(new Random().nextInt(instances.size()));
    }
}
