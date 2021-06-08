package github.zzz.rpc.core.remoting.transport.netty.client;

import github.zzz.rpc.common.utils.RpcMessageChecker;
import github.zzz.rpc.core.codec.CommonDecoder;
import github.zzz.rpc.core.codec.CommonEncoder;
import github.zzz.rpc.core.remoting.RpcClient;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
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

/**
 * Netty实现的BIO版本的传输协议
 * 实现Client中的sendRequest的功能
 * @author zzz
 */
public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstrap;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }

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
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 初始化通道对象
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // rpcResponse -> byteBuf
                        pipeline.addLast(new CommonDecoder())
                                // byteBuf -> rpcRequest
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 发送消息到服务端
     * @param rpcRequest 请求对象
     * @return rpcResponse对象或null
     */
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            // 通过bootstrap连接服务端
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
}
