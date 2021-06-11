package github.zzz.rpc.core.remoting.transport.netty.client;

import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.common.utils.RpcMessageChecker;
import github.zzz.rpc.core.codec.CommonDecoder;
import github.zzz.rpc.core.codec.CommonEncoder;
import github.zzz.rpc.core.registry.NacosServiceDiscovery;
import github.zzz.rpc.core.registry.ServiceDiscovery;
import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import github.zzz.rpc.core.serializer.CommonSerializer;
import github.zzz.rpc.core.serializer.JsonSerializer;
import github.zzz.rpc.core.serializer.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Netty实现的BIO版本的传输协议
 * 实现Client中的sendRequest的功能
 * 添加nacos作为注册中心之后，host和port是通过nacos获取的，而不是本身声明的
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Bootstrap bootstrap;
    private CommonSerializer serializer;
    private ServiceDiscovery serviceDiscovery = new NacosServiceDiscovery();

    static {
        // 创建线程组和配置client的启动对象，随时准备连接服务端
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        // option()设置的是服务端用于接收进来的连接，也就是boosGroup线程
        // childOption()设置的是提供给父管道接收到的连接，也就是workerGroup线程
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 是否开启tcp底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    /**
     * 发送消息到服务端
     * @param rpcRequest 请求对象
     * @return rpcResponse对象或null
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null){
            logger.error("Did not set the serializer");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 初始化通道
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                // rpcResponse -> byteBuf
                pipeline.addLast(new CommonDecoder())
                        // byteBuf -> rpcRequest
                        .addLast(new CommonEncoder(serializer))
                        .addLast(new NettyClientHandler());
            }
        });

        try {
            // 通过服务发现得到对应的地址
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            // 通过bootstrap连接服务端
            String host = inetSocketAddress.getHostName();
            int port = inetSocketAddress.getPort();
            ChannelFuture future = bootstrap.connect(host, port).sync();
            logger.info("Client connected to the server {}:{}", host, port);
            // 通过Channel发送请求消息
            Channel channel = future.channel();
            logger.info("Start sending the message");
            if (channel != null){
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()){
                        logger.info(String.format("Client send a message: %s", rpcRequest.toString()));
                    } else{
                        logger.error("Error happens when sending a message: ", future1.cause());
                    }
                });
                // 阻塞等待，直到Channel关闭
                channel.closeFuture().sync();
                // 从管道中读取到处理好的数据
                // 取出服务端返回对象，通过该种方法可以阻塞获得返回结果
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                // 因为之前的NettyClientHandler将response对象放到了ctx中，所以这里就可以直接取出了
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest,rpcResponse);
                return rpcResponse.getData();
            }
        } catch (InterruptedException  e){
            logger.error("Error happens when sending a message: ", e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
