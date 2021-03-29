package github.zzz.rpc.common.enumeration;

import lombok.Getter;

/**
 * 响应状态码
 * @author zzz
 */
@Getter
public enum ResponseCode {

    /**
     * 调用成功时候返回的状态码和信息
     */
    SUCCESS(200,"Successfully use the method"),

    /**
     * 失败时候调用的状态码和信息
     */
    FAIL(500,"Fail to use the method"),

    /**
     * 没有找到方法时候的状态码和信息
     */
    METHOD_NOT_FOUND(500,"Did not find the method"),

    /**
     * 没有找到类时候的状态码和信息
     */
    CLASS_NOT_FOUND(500,"Did not find the class");


    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
