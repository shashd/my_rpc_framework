package github.zzz.rpc.core.remoting.transport.netty.server;

import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.core.codec.CommonDecoder;
import github.zzz.rpc.core.codec.CommonEncoder;
import github.zzz.rpc.core.provider.ServiceProvider;
import github.zzz.rpc.core.provider.ServiceProviderImpl;
import github.zzz.rpc.core.registry.NacosServiceRegistry;
import github.zzz.rpc.core.registry.ServiceRegistry;
import github.zzz.rpc.core.remoting.RpcServer;
import github.zzz.rpc.core.serializer.CommonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * Netty实现的BIO版本的传输协议
 * 实现server的监听的功能
 * 添加了Nacos之后通过ServiceRegistry和ServiceProvider来注册服务和实例
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private CommonSerializer serializer;

    private String host;
    private int port;
    private ServiceRegistry serviceRegistry;
    private ServiceProvider serviceProvider;

    public NettyServer(String host, int port){
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void start(int port) {
        if (serializer == null){
            logger.error("Did not set the serializer");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 默认的线程数是cpu核数的两倍
        // 监听客户端连接，专门负责与客户端创建连接，并把连接注册到workerGroup的Selector中
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用于处理每一个连接发生的读写事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // option()设置的是服务端用于接收进来的连接，也就是boosGroup线程
            // childOption()设置的是提供给父管道接收到的连接，也就是workerGroup线程
            bootstrap.group(bossGroup, workerGroup)
                    // 设置服务器通道实现类型
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 是否开启tcp底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。
                    // TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 初始化通道对象
                    // 责任链上有多个处理器，每个处理器都会对数据进行加工，并将处理后的数据传给下一个处理器
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 解码器，是因为没有对合适的对象进行编码和解码吗？
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            // 数据处理器
                            pipeline.addLast(new NettyServerHandler());

                        }
                    });
            // 绑定端口，同步等待绑定成功
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e){
            logger.error("Occur exception when starting the server : ", e);
        } finally {
            // 关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (service == null){
            logger.info("publishService - service is not found");
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        String serviceName = serviceClass.getCanonicalName();
        serviceProvider.addServiceProvider(service,serviceName);
        serviceRegistry.register(serviceName,new InetSocketAddress(host,port));
        // 带来的问题是一个服务端只能注册一个服务，而不是多个，因为直接调用了start，需要进行修改
        start(port);
    }
}
