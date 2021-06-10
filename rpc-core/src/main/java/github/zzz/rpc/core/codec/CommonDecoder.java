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
 *
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 1. 读取协议中的魔数
        int magicNumber = getMagicNumber(byteBuf);
        // 2. 读取包的类型，通过反射设定class
        Class<?> packageClass = getPackageClass(byteBuf);
        // 3. 序列化方式
        CommonSerializer serializer  = getSerializer(byteBuf);
        // 4. 读取数据长度和具体的数据，将二进制数据读取到生成的二进制数组中
        byte[] bytes = getBytes(byteBuf);
        // 5. 使用对应正确的序列化器来解码
        Object object = serializer.deserialize(bytes,packageClass);
        // 但是感觉这个并没有用处吧
        list.add(object);
    }

    /**
     * Get magic number
     * @param byteBuf data container
     * @return magicNumber
     */
    public int getMagicNumber(ByteBuf byteBuf){
        int magicNumber = byteBuf.readInt();
        if (magicNumber != MAGIC_NUMBER){
            logger.error("Unknown package : {}", magicNumber);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        return magicNumber;
    }

    /**
     * Get package class
     * @param byteBuf data container
     * @return packageClass: RpcRequest/RpcResponse
     */
    public Class<?> getPackageClass(ByteBuf byteBuf){
        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()){
            System.out.println("the package type is request");
            packageClass = RpcRequest.class;
            System.out.println("decoding request : " + packageClass.toString());
        }else if (packageCode == PackageType.RESPONSE_PACK.getCode()){
            System.out.println("the package type is response");
            packageClass = RpcResponse.class;
            System.out.println("decoding response : " + packageClass.toString());
        }else{
            logger.error("UNKNOWN PACKAGE : {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        return packageClass;
    }

    /**
     * Get serializer by reading the serializerCode
     * @param byteBuf data container
     * @return CommonSerializer
     */
    public CommonSerializer getSerializer(ByteBuf byteBuf){
        int serializerCode = byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null){
            logger.error("UNKNOWN SERIALIZER : {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        return serializer;
    }


    /**
     * Get data from byte[]
     * @param byteBuf data container
     * @return byte[]
     */
    public byte[] getBytes(ByteBuf byteBuf){
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
