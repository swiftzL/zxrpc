package cn.zl.rpcserver.ratelimiter;

/**
 * @Author: zl
 * @Date: 2021/5/14 10:45 上午
 */
@FunctionalInterface
public interface Function<T, R> {

    R run(T t) throws Exception;

}
