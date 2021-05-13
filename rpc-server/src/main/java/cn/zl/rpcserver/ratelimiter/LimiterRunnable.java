package cn.zl.rpcserver.ratelimiter;

/**
 * @Author: zl
 * @Date: 2021/5/13 3:45 下午
 */
@FunctionalInterface
public interface LimiterRunnable {

    void run();

    default Runnable unLimiter() {
        return () -> {
            run();
        };

    }

}
