package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.execption.SerializerTypeNotFound;
import cn.zl.zxrpc.rpccommon.serializer.kryo.KryoBuilder;

import java.lang.reflect.ParameterizedType;

public class Serializer<R> implements RpcSerializer {

    private RpcSerializer delegate;
    private Class<R> clazz;
    public static Serializer getInstance(SerializerType type,Class<?> clazz) {
        switch (type) {
            case KRYO:
                return new Serializer(new KryoBuilder(),clazz);
        }
        throw new SerializerTypeNotFound();
    }

    public Serializer(RpcSerializer rpcSerializer,Class<R> clazz) {
        this.delegate = rpcSerializer.build();
        this.clazz = clazz;
    }



    public Serializer() {

    }


    @Override
    public byte[] encode(Object o) {
        return delegate.encode(o);
    }

    @Override
    public R decode(byte[] bytes) {
        return (R) delegate.decode(bytes,this.clazz);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return delegate.decode(bytes, clazz);
    }


    @Override
    public Serializer<R> build() {
        this.delegate = delegate.build();
        return this;
    }

    @Override
    public Serializer<R> register(Class clazz) {
        this.delegate = delegate.register(clazz);
        return this;
    }

    @Override
    public Serializer<R> register(Class... classes) {
        this.delegate = this.delegate.register(classes);
        return this;
    }

}
