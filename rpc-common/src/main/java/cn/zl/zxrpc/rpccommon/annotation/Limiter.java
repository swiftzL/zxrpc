package cn.zl.zxrpc.rpccommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

/**
 * @Author: zl
 * @Date: 2021/5/13 11:40 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Limiter {
    // intervalTime
    int intervalTime();

    int count();
}
