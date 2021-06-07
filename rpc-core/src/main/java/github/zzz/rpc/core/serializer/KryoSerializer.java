package github.zzz.rpc.core.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import github.zzz.rpc.common.enumeration.SerializerCode;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Kryo序列化类，效率高
 * 抽取出的公共方法在CommonSerializer中
 * @author zzz
 */
public class KryoSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /**
     * 因为Kryo并不是线程安全的，所以每个线程都需要有一个自己的kryo
     * ThreadLocal存放Kryo对象
     */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() ->{
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        // 默认值为true,是否关闭注册行为,关闭之后可能存在序列化问题，一般推荐设置为 true
        kryo.setReferences(true);
        // 默认值为false,是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    /**
     * 将对象序列化成byte数组
     * @param object 对象
     * @return 二进制数组
     */
    @Override
    public byte[] serialize(Object object){
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // 序列化成byte数组
            kryo.writeObject(output,object);
            kryoThreadLocal.remove();
            return output.toBytes();
        }catch (Exception e){
            logger.info("Fail to serialize");
            throw new SerializationException("Fail to serialize");
        }
    }

    /**
     * 将二进制数组转换为对象
     * @param bytes 二进制数据
     * @param clazz 对象类型
     * @return 对象
     */
    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)){
            Kryo kryo = kryoThreadLocal.get();
            Object object = kryo.readObject(input,clazz);
            kryoThreadLocal.remove();
            return object;
        }catch (Exception e){
            logger.info("Fail to deserialize");
            throw new SerializationException("Fail to deserialize");

        }

    }

    @Override
    public int getCode() {
        return SerializerCode.KRYO.getCode();
    }
}
