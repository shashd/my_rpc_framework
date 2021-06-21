package github.zzz.rpc.core.remoting.transport.netty.client;

import com.sun.xml.internal.ws.util.CompletedFuture;
import github.zzz.rpc.common.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 未处理的请求
 */
public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedResponseFutures =
            new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future){
        unprocessedResponseFutures.put(requestId,future);
    }

    public void remove(String requetId){
        unprocessedResponseFutures.remove(requetId);
    }

    public void complete(RpcResponse rpcResponse){
        // 得到map中的结果
        CompletableFuture<RpcResponse> future  =unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if (null != future){
            future.complete(rpcResponse);
        } else{
            throw new IllegalStateException();
        }
    }

}
