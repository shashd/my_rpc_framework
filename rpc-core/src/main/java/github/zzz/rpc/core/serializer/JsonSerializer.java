package github.zzz.rpc.core.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.zzz.rpc.common.exception.SerializeException;
import github.zzz.rpc.common.enumeration.SerializerCode;
import github.zzz.rpc.common.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json实现序列化的方式一般使用Jackson包，通过ObjectMapper类来进行操作
 * @author zzz
 */
public class JsonSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 序列化方法接口
     * @param object 对象
     * @return 二进制数据
     */
    @Override
    public byte[] serialize(Object object){
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e){
            String message = "Error happens when serializing : ";
            logger.error(message, e);
            throw new SerializeException(message);
        }
    }

    /**
     * 反序列化方法
     * @param bytes 二进制数据
     * @param clazz 对象类型
     * @return 对象
     */
    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz){
        try {
            Object object = objectMapper.readValue(bytes,clazz);
            if (object instanceof RpcRequest){
                // 转换为原来的rpcRequest的实例
                object = handleRequest(object);
            }
            return object;
        } catch (IOException e){
            String message = "Error happens when deserializing : ";
            logger.error(message, e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 因为无法保证反序列化后仍然为原实例类型
     * 所以就需要重新对其参数进行判断和处理
     * 而且仅仅是因为转化为JSON字符串之后是会丢失对象的类型信息的，其他的序列化方式并不会遇到这种情况
     * @param object 反序列化得到的对象
     * @return rpcRequest
     */
    private Object handleRequest(Object object) throws IOException{
        RpcRequest rpcRequest = (RpcRequest)object;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++){
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            // 检查参数的类是否相等，不同意外这反序列化失败了
            if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                // 使用参数中每个实例的实际类来辅助反序列化
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes,clazz);
            }
        }
        return rpcRequest;
    }

    /**
     * 序列化器和反序列化器的编号
     * @return Code
     */
    @Override
    public int getCode(){
        return SerializerCode.JSON.getCode();
    }

}
