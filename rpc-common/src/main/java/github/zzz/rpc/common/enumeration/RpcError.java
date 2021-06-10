package github.zzz.rpc.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用异常的情况
 * 可以直接返回对应code的RpcException的信息
 * todo: 完善更多的exception的情况
 *
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    /**
     * 包含详细的错误内容
     */
    UNKNOWN_ERROR("UNKNOWN ERROR"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("SERVICE NOT IMPLEMENT ANY INTERFACE"),
    SERVICE_NOT_FOUND("SERVICE NOT FOUND"),
    UNKNOWN_PROTOCOL("UNKNOWN PACKAGE"),
    UNKNOWN_PACKAGE_TYPE("UNKNOWN PACKAGE TYPE"),
    UNKNOWN_SERIALIZER("UNKNOWN SERIALIZER"),
    SERVICE_INVOCATION_FAILURE("SERVICE INVOCATION FAILURE"),
    RESPONSE_NOT_MATCH("RESPONSE NOT MATCH"),
    SERIALIZER_NOT_FOUND("SERIALIZER_NOT_FOUND");

    private final String message;
}
