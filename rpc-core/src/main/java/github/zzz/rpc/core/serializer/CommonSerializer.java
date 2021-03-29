package github.zzz.rpc.core.serializer;

/**
 * 通用的序列化反序列化接口
 * 包含多种序列化方式的实现
 * @author zzz
 */
public interface CommonSerializer {

    /**
     * 分别是四种实现序列化的方案
     * 默认使用Kryo来实现
     */
    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer PROTOBUF_SERIALIZER = 3;
    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;


    /**
     * 序列化方法接口
     * @param object 对象
     * @return 二进制数据
     */
    byte[] serialize(Object object);

    /**
     * 反序列化方法
     * @param bytes 二进制数据
     * @param clazz 对象类型
     * @return 对象
     */
    Object deserialize(byte[] bytes, Class<?> clazz);

    /**
     * 序列化器和反序列化器的编号
     * @return Code
     */
    int getCode();
}
