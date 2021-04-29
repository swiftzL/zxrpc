package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.extension.SPI;

@SPI
public interface RpcSerializer {

    byte[] encode(Object o);

    Object decode(byte[] bytes);

    <T> T decode(byte[] bytes,Class<T> clazz);

    RpcSerializer build();

    RpcSerializer register(Class clazz);

    RpcSerializer register(Class... classes);


}
