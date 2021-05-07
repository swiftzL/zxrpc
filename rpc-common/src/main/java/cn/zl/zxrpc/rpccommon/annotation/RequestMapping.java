package cn.zl.zxrpc.rpccommon.annotation;

import cn.zl.zxrpc.rpccommon.message.HttpMethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: zl
 * @Date: 2021/5/7 3:32 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value()default "";
    HttpMethodType httpMethodType();

}
