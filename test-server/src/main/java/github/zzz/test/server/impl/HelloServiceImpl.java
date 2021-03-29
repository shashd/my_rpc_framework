package github.zzz.test.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.zzz.rpc.api.Hello;
import github.zzz.rpc.api.HelloService;

/**
 * HelloService的实现类，这个项目的pom中需要实现依赖
 * @author zzz
 */
public class HelloServiceImpl implements HelloService {

    /**
     * 获取 slf4j 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);


    @Override
    public String sayHello(Hello hello){
        logger.info("get message: {}", hello.getMessage());
        return "This is sayHello implementation function";
    }

}
