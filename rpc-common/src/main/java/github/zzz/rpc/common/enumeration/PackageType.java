package github.zzz.rpc.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义协议中的字段
 * 表示该请求是调用请求还是响应请求
 *
 */
@Getter
@AllArgsConstructor
public enum PackageType {

    /**
     * 声明是request的消息类型
     */
    REQUEST_PACK(0),
    /***
     * 声明是response的消息类型
     */
    RESPONSE_PACK(1);

    private final int code;

}
