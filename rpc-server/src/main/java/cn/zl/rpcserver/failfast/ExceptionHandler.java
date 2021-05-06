package cn.zl.rpcserver.failfast;

import cn.zl.rpcserver.handler.codec.MessageType;

/**
 * @Author: zl
 * @Date: 2021/5/4 8:14 下午
 */
public @interface ExceptionHandler {
    Class<? extends Exception> value();
    MessageType type();
}
