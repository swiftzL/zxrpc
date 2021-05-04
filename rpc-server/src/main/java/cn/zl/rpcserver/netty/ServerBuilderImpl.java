package cn.zl.rpcserver.netty;

import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.server.Server;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.rpcserver.service.ServerMethodDefinition;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;

import java.util.function.Supplier;

public abstract class ServerBuilderImpl<T extends ServerBuilderImpl<T>> extends ServerBuilder<T> {

    public ServerBuilderImpl() {
    }

    protected abstract ServerBuilder<?> delegate();


    @Override
    public T addService(RpcServiceMethod service) {
        delegate().addService(service);
        return selfT();
    }

    @Override
    public T addService(Class<?> clazz) {
        delegate().addService(clazz);
        return selfT();
    }

    @Override
    public T addService(Class<?> clazz, Object object) {
        delegate().addService(clazz, object);
        return selfT();
    }

    @Override
    public T addProtocolJudge(ProtocolJudge protocolJudge) {
        delegate().addProtocolJudge(protocolJudge);
        return selfT();
    }

    @Override
    public T addSerializerRequest(Supplier<Serializer<RpcRequest>> rpcRequestSerializerSupplier) {
        delegate().addSerializerRequest(rpcRequestSerializerSupplier);
        return selfT();
    }

    @Override
    public T addSerializerResponse(Supplier<Serializer<RpcResponse>> rpcResponseSerializerSupplier) {
        delegate().addSerializerResponse(rpcResponseSerializerSupplier);
        return selfT();
    }

    @Override
    public Server build() {
        return delegate().build();
    }
}
