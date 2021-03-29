package github.zzz.rpc.common.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * client向着server发送的请求对象
 * 能够序列化，包含id，接口名称，方法，方法参数和类型，以及是否为心跳包的判断
 * @author zzz
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数
     */
    private Object[] parameters;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 是否为心跳包
     */
    private Boolean hearBeat;
}
