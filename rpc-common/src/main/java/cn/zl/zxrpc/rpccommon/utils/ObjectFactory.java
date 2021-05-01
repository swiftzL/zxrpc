package cn.zl.zxrpc.rpccommon.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectFactory<T> {

    private Supplier<T> provider;
    private volatile T object;

    public static ObjectFactory getInstance(Supplier<?> provider) {
        return new ObjectFactory(provider);
    }

    public ObjectFactory(Supplier<T> provider) {
        this.provider = provider;
    }

    public T getObject() {
        if (object != null){
            return object;
        }
        synchronized (this){
            if(object==null){
                object = provider.get();
            }
            return object;
        }
    }
}
