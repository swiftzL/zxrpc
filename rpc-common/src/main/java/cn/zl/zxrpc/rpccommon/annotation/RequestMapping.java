package cn.zl.zxrpc.rpccommon.annotation;

import cn.zl.zxrpc.rpccommon.message.HttpMethodType;

/**
 * @Author: zl
 * @Date: 2021/5/7 3:32 下午
 */
public @interface RequestMapping {
    String value()default "";
    HttpMethodType httpMethodType();

}
