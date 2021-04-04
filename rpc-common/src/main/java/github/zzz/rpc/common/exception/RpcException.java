package github.zzz.rpc.common.exception;

import github.zzz.rpc.common.enumeration.RpcError;

/**
 * RPC调用异常类
 * 自定义业务相关的异常，继承RuntimeException
 * @author zzz
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcError rpcError, String detail){
        super(rpcError.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause){
        super(message, cause);
    }

    public RpcException(RpcError rpcError){
        super(rpcError.getMessage());
    }

}
