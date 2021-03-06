package github.zzz.rpc.common.entity;


import github.zzz.rpc.common.enumeration.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * server对client的响应消息对象
 * 类型设置为泛型
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对象的请求id
     */
    private String requestId;

    /**
     * 响应状态码
     */
    private Integer statusCode;

    /**
     * 响应状态补充数据
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 快速生成成功的响应对象
     * todo: 设置requestId才是
     * @param data 数据
     * @return response
     */
    public static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<T>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setRequestId(requestId);
        response.setData(data);
        return response;
    }

    /**
     * 快速生成失败的响应对象T
     * @param code 失败状态码
     * @return response
     */
    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId){
        RpcResponse<T> response = new RpcResponse<T>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}
