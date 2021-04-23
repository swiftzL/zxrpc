package cn.zl.zxrpc.rpccommon.serializer;

import java.util.HashSet;
import java.util.Set;

public abstract class SerializerAdapter implements RpcSerializer {

    protected Set<Class<?>> registerClass = new HashSet<>();

    @Override
    public RpcSerializer build() {
        return null;
    }

    @Override
    public byte[] encode(Object o) {
        return null;
    }

    @Override
    public Object decode(byte[] bytes) {
        return null;
    }

    public RpcSerializer  register(Class clazz){
        registerClass.add(clazz);
        return this;
    }
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
