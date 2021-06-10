package github.zzz.rpc.core.remoting.transport.socket.util;

import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import github.zzz.rpc.common.enumeration.PackageType;
import github.zzz.rpc.core.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 类似于encode的功能
 * 对数据进行序列化操作并且写成二进制数据
 */
public class ObjectWriter {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer)
            throws IOException {
        // 写入magicNumber
        outputStream.write(intToBytes(MAGIC_NUMBER));
        // 写入packageCode
        if (object instanceof RpcRequest){
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        // 写入serializerCode
        outputStream.write(intToBytes(serializer.getCode()));
        // 写入长度和数据
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * 将int转换成bytes数组
     * @param value 转换数据
     * @return 二进制数组
     */
    public static byte[] intToBytes(int value){
        byte[] des = new byte[4];
        des[3] =  (byte) ((value>>24) & 0xFF);
        des[2] =  (byte) ((value>>16) & 0xFF);
        des[1] =  (byte) ((value>>8) & 0xFF);
        des[0] =  (byte) (value & 0xFF);
        return des;
    }

}
