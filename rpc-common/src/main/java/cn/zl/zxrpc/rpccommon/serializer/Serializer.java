package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.execption.SerializerTypeNotFound;
import cn.zl.zxrpc.rpccommon.serializer.kryo.KryoBuilder;

public class Serializer implements RpcSerializer {

    private RpcSerializer delegate;


    public static RpcSerializer getInstance(SerializerType type){
        switch (type){
            case KRYO:
                return new Serializer(new KryoBuilder());
        }
        throw new SerializerTypeNotFound();
    }
    public Serializer(RpcSerializer rpcSerializer){
        this.delegate= rpcSerializer;
    }

    @Override
    public byte[] encode(Object o) {
        return delegate.encode(o);
    }

    @Override
    public Object decode(byte[] bytes) {
        return delegate.encode(bytes);
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return delegate.decode(bytes,clazz);
    }

    @Override
    public RpcSerializer build() {
        return delegate.build();
    }

    @Override
    public RpcSerializer register(Class clazz) {
        return delegate.register(clazz);
    }
}
