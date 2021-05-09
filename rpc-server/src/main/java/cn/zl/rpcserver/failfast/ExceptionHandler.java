package cn.zl.rpcserver.failfast;

import cn.zl.rpcserver.handler.codec.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: zl
 * @Date: 2021/5/4 8:14 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {
    Class<? extends Exception> value();
    MessageType type();
}
