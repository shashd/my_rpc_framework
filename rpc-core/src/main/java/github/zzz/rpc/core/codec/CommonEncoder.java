package github.zzz.rpc.core.codec;

import github.zzz.rpc.common.enumeration.PackageType;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.core.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 讲Message转成Byte数组
 *
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer){
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 1. 魔数写入
        byteBuf.writeInt(MAGIC_NUMBER);
        // 2. 写入消息类型
        if (o instanceof RpcRequest){
            byteBuf.writeInt(PackageType.REQUEST_PACK.getCode());
        }else{
            byteBuf.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 3. 写入序列化类型
        byteBuf.writeInt(serializer.getCode());
        // 4. 接口实现的序列化方法
        byte[] bytes = serializer.serialize(o);
        // 5. 写入信息长度
        byteBuf.writeInt(bytes.length);
        // 6. 写入具体的数据
        byteBuf.writeBytes(bytes);

    }
}
