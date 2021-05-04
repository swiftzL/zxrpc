package cn.zl.rpcserver.service;

import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;

import java.lang.reflect.Method;

public class RpcServiceMethod<T> extends ServiceMethod {

    private Serializer<RpcResponse<T>> serializer;

    private Serializer<RpcRequest> rpcRequestSerializer;

    public RpcServiceMethod(Class<?> clazz, Object o, Method m, Serializer<RpcResponse<T>> serializer
            , Serializer<RpcRequest> rpcRequestSerializer) {
        //create rpc service method
        super(o, m,clazz);
        this.serializer = serializer;
        this.rpcRequestSerializer = rpcRequestSerializer;
    }


    public RpcResponse<T> invoke(Object... args) {
        Object object = super.invoke(args);
        return new RpcResponse("", 12, "", (T) object);
    }

    public byte[] getResponseBytes(Object... args) {
        return this.serializer.encode(invoke(args));
    }

    public byte[] getResponseBytes(RpcRequest rpcRequest) {
        return this.serializer.encode(invoke(rpcRequest.getArgs()));
    }

    public byte[] getResponseBytes(byte[] rpcRequestBytes) {
        RpcRequest rpcRequest = this.rpcRequestSerializer.decode(rpcRequestBytes);
        return getResponseBytes(rpcRequest);
    }

    public Serializer<RpcResponse<T>> getSerializer() {
        return serializer;
    }

    public String methodSignature() {
        return super.methodSignature();
    }

}
