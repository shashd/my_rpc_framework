package github.zzz.rpc.common.utils;

import github.zzz.rpc.common.enumeration.ResponseCode;
import github.zzz.rpc.common.enumeration.RpcError;
import github.zzz.rpc.common.exception.RpcException;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查响应和请求
 * @author zzz
 */
public class RpcMessageChecker {

    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    private RpcMessageChecker(){}

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse){
        if (rpcResponse == null){
            logger.error("Fail to call the function, serviceName : {}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        // 检查id是否对应，即是否对应的响应
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        // 检查状态码是否为空或者是否成功
        if (rpcResponse.getStatusCode() == null ||
                !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            logger.error("Fail to call the function, serviceName : {}, ,RpcResponse:{}",
                    rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,
                    INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());

        }
    }
}
