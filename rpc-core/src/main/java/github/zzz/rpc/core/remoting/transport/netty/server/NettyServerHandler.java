package github.zzz.rpc.core.remoting.transport.netty.server;

import github.zzz.rpc.common.factory.SingletonFactory;
import github.zzz.rpc.core.handler.RequestHandler;
import github.zzz.rpc.core.registry.DefaultServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 直接和rpcRequest打交道
 * 数据时从远程主机到用户应用程序则是“入站(inbound)”
 * 这个前后应该是有两个版本，所以先初始化一个的?
 * @author zzz
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    /**
     * 收到request之后执行requestHandler之中的方法
     * 封装为RpcResponse并且返回
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            logger.info("The server receive the request : {}", msg);
            // 1. 找到注册的服务
            String interfaceName= msg.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            String requestId = msg.getRequestId();
            // 2. 通过反射调用方法得到结果并且监听通道
            Object result = requestHandler.handle(msg,service);
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result,requestId));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error happens when calling the process : ");
        cause.printStackTrace();
        ctx.close();
    }
}
