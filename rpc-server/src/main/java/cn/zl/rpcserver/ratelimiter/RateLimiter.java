package cn.zl.rpcserver.ratelimiter;

import cn.zl.zxrpc.rpccommon.execption.LimiterException;

import java.util.function.Supplier;

/**
 * @Author: zl
 * @Date: 2021/5/13 3:44 下午
 */
public interface RateLimiter {

    static LimiterRunnable decorateCheckedRunnable(RateLimiter rateLimiter, int permits, LimiterRunnable runnable) {
        return () -> {
            waitForPermission(rateLimiter, permits);
            try {
                runnable.run();
//                rateLimiter.onSuccess();
            } catch (Exception exception) {
//                rateLimiter.onError(exception);
                throw exception;
            }
        };
    }

    static RateLimiter of(String name, RateLimiterConfig rateLimiterConfig) {
        AtomicRateLimiter atomicRateLimiter = new AtomicRateLimiter(name, rateLimiterConfig);
        return atomicRateLimiter;

    }


    static <T> SupplierFunction<T> decorateCheckedSupplier(RateLimiter rateLimiter, int permits, Supplier<T> supplier) {
        return () -> {
            waitForPermission(rateLimiter, permits);
            try {
                T result = supplier.get();
                return result;
            } catch (Exception e) {
                throw e;
            }
        };
    }

    boolean acquirePermission(int permits);

    static void waitForPermission(RateLimiter rateLimiter, int permits) {//校验权限
        boolean isPermission = rateLimiter.acquirePermission(permits);
        if (!isPermission) {
            throw new LimiterException();
        }
    }
}
