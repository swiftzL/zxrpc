package cn.zl.rpcserver.netty;

import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.server.Server;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.rpcserver.service.ServerMethodDefinition;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.register.Register;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;

import java.util.function.Supplier;

public abstract class ServerBuilder<T extends ServerBuilder> {

    public static ServerBuilder<?> forPort(int port) {
        return ServerProvider.serverProvider().builderForPort(port);
    }


    // add service
    public abstract T addService(RpcServiceMethod service);

    public abstract T addService(Class<?> clazz);

    public abstract T addService(Class<?> clazz, Object object);


    //filter


    //intercept


    //server register

    public abstract T register(Register register);


    //ProtocolJudge

    public abstract T addProtocolJudge(ProtocolJudge protocolJudge);


    public abstract T addSerializerRequest(
            Supplier<Serializer<RpcRequest>> rpcRequestSerializerSupplier);

    public abstract T addSerializerResponse(Supplier<Serializer<RpcResponse>> rpcResponseSerializerSupplier);

    public abstract Server build();

    public T selfT() {
        return (T) this;
    }


}
