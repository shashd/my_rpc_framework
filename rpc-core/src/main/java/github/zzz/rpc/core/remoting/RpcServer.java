package github.zzz.rpc.core.remoting;

/**
 * 服务器端抽象接口
 * @author zzz
 */
public interface RpcServer {
    /**
     * 服务器端启动监听和处理的逻辑
     * @param port 端口号
     */
    void start(int port);
}
