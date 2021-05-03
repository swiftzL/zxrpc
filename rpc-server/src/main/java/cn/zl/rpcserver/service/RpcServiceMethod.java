package cn.zl.rpcserver.service;

import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;

import java.lang.reflect.Method;

public class RpcServiceMethod<T> extends ServiceMethod {

    private Serializer<RpcResponse<T>> serializer;

    public RpcServiceMethod(Object o, Method m, Serializer<RpcResponse<T>> serializer) {
        //create rpc service method
        super(o,m);
        this.serializer = serializer;
    }

    public RpcResponse<T> invoke(Object... args) {
        Object object = super.invoke(args);
        return new RpcResponse("", 12, "", (T) object);
    }

    public byte[] getResponseBytes(Object... args) {
        return this.serializer.encode(invoke(args));
    }


}
