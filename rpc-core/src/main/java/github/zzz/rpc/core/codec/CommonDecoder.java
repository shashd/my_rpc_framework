package github.zzz.rpc.core.codec;

import github.zzz.rpc.common.enumeration.PackageType;
import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import github.zzz.rpc.core.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解码器
 * 将二进制数组转换成原来的对象
 * @author zzz
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);

    /**
     * 和encoder对应的魔数
     */
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    // todo: 拆分更加详细的几个功能
    @Override
    public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 1. 读取协议中的魔数
        int magicNumber = byteBuf.readInt();
        if (magicNumber != MAGIC_NUMBER){
            logger.error("Unknown package : {}", magicNumber);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        // 2. 读取包的类型，通过反射设定class
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()){
            packageClass = RpcRequest.class;
        }else if (packageCode == PackageType.RESPONSE_PACK.getCode()){
            packageClass = RpcResponse.class;
        }else{
            logger.error("UNKNOWN PACKAGE : {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        // 3. 序列化方式
        int serializerCode = byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null){
            logger.error("UNKNOWN SERIALIZER : {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        // 4. 读取数据长度和具体的数据，将二进制数据读取到生成的二进制数组中
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        // 5. 使用对应正确的序列化器来解码
        Object object = serializer.deserialize(bytes,packageClass);
        // todo: 为什么还要添加到list中呢？
        list.add(object);
    }
}
