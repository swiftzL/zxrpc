package cn.zl.rpcclient.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zl
 * @Date: 2021/5/11 1:53 下午
 */
public class ResponseHolder {
    private static Map<String, CompletableFuture> responseMap = new ConcurrentHashMap<>();


    public static CompletableFuture getFuture(String requestId) {
        return responseMap.get(requestId);
    }

    public static void completable(String requestId, Object o) {
        //remove the requestId and return future
        CompletableFuture completableFuture = responseMap.remove(requestId);
        if (completableFuture != null) {
            completableFuture.complete(o);
        } else {
            throw new IllegalArgumentException(requestId + "is not found");
        }
    }

    public static void remove(String requestId) {
        responseMap.remove(requestId);
    }


}
