package cn.zl.rpcserver.netty;

import cn.zl.rpcserver.service.ServerMethodDefinition;

public abstract class ServerBuilder<T extends ServerBuilder> {

    public static ServerBuilder<?> forPort(int port) {
        return ServerProvider.serverProvider().builderForPort(port);
    }


    // add service
    public abstract T addService(ServerMethodDefinition service);

    public abstract T addService(Class<?> clazz);

    public T selfT(){
        return (T)this;
    }



}
