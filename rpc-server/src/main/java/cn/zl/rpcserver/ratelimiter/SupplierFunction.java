package cn.zl.rpcserver.ratelimiter;

import java.util.function.Supplier;

/**
 * @Author: zl
 * @Date: 2021/5/13 4:43 下午
 */

@FunctionalInterface
public interface SupplierFunction<T> extends Supplier<T> {


    T apply();

    @Override
    default T get(){
        return apply();
    }
}
