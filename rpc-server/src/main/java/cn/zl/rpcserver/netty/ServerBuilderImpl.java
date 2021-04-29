package cn.zl.rpcserver.netty;

import cn.zl.rpcserver.service.ServerMethodDefinition;

public abstract class ServerBuilderImpl<T extends ServerBuilderImpl<T>> extends ServerBuilder<T> {

    public ServerBuilderImpl(){}

    protected abstract ServerBuilder<?> delegate();


    @Override
    public T addService(ServerMethodDefinition service) {
        delegate().addService(service);
        return selfT();
    }

    @Override
    public T addService(Class<?> clazz) {
        delegate().addService(clazz);
        return selfT();
    }


}
