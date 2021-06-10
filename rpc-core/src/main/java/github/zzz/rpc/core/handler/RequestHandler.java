package github.zzz.rpc.core.handler;

import github.zzz.rpc.common.enumeration.ResponseCode;
import github.zzz.rpc.common.entity.RpcRequest;
import github.zzz.rpc.common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 进行过程调用的处理器，处理具体的逻辑
 * 处理RpcRequest和服务对象（RequestHandlerThread传递的）
 *
 */
public class RequestHandler {


    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);


    /**
     * 执行主要逻辑，调用invokeTargetMethod并且记录日志
     * @param rpcRequest rpcRequest对象
     * @param service 服务对象
     * @return rpcResponse对象
     */
    public Object handle(RpcRequest rpcRequest, Object service){
        Object result = null;
        try {
            result = invokeTargetMethod(rpcRequest, service);
            // Service : github.zzz.rpc.api.HelloService successfully use the Method : sayHello
            logger.info("Service : {} successfully use the Method : {}",rpcRequest.getInterfaceName(),
                    rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e){
            logger.error("Error happens when using or sending : ", e);
        }
        return result;
    }

    /**
     * 原本WorkerThread中的run部分中通过反射得到方法执行后的结果
     * @param rpcRequest rpcRequest对象
     * @param service 服务对象
     * @return response对象
     * @throws IllegalAccessException 异常1
     * @throws InvocationTargetException 异常2
     */
    public Object invokeTargetMethod(RpcRequest rpcRequest, Object service)
            throws IllegalAccessException, InvocationTargetException{
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e){
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND,rpcRequest.getRequestId());
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
