package cn.zl.zxrpc.rpccommon.annotation;

public @interface RpcConsumer {

    int retry() default 0;

}
