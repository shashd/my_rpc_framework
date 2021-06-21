package github.zzz.rpc.core.hook;

import github.zzz.rpc.common.utils.NacosUtil;
import github.zzz.rpc.common.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook(){
        return shutdownHook;
    }

    public void addClearAAllHook(){
        logger.info("Clear all the services after closing the connection");
        // 创建新线程执行clear的操作
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 清理注册中心的服务
            NacosUtil.clearRegistry();
            // 关闭线程池
            ThreadPoolFactory.shutDownAll();
        }));
    }

}
