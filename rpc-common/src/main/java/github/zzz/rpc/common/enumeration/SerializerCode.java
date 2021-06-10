package github.zzz.rpc.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化和反序列化器
 * 这个目前也并没有被使用到
 *
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {

    /**
     * 表示使用KRYO
     */
    KRYO(0),

    /**
     * 表示使用JSON
     */
    JSON(1),

    /**
     * 表示使用HESSIAN
     */
    HESSIAN(2),

    /**
     * 表示使用PROTOBUF
     */
    PROTOBUF(3);

    private final int code;


}
