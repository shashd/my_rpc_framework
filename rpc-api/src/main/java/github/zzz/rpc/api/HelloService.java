package github.zzz.rpc.api;

import java.io.Serializable;

/**
 * 测试api的接口
 *
 */
public interface HelloService{

    /**
     * 测试的方法
     * @param hello DI注入实例
     * @return String
     */
    String sayHello(Hello hello);
}
