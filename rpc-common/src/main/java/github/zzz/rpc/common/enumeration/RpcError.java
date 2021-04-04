package github.zzz.rpc.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用异常
 * todo: 完善更多的exception的情况
 * @author zzz
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    /**
     * 包含详细的错误内容
     */
    UNKNOWN_ERROR("UNKNOWN ERROR"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("SERVICE NOT IMPLEMENT ANY INTERFACE"),
    SERVICE_NOT_FOUND("SERVICE NOT FOUND");


    private final String message;
}
