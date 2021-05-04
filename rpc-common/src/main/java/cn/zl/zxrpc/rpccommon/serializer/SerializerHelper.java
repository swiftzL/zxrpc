package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.execption.SerializerTypeNotFound;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.kryo.KryoBuilder;

/**
 * @Author: zl
 * @Date: 2021/5/4 4:27 下午
 */
public class SerializerHelper {

    public static Serializer getDefaultRequestSerializer() {
        return new Serializer(new KryoBuilder(), RpcRequest.class);
    }

    public static Serializer getDefaultResponseSerializer() {
        return new Serializer(new KryoBuilder(), RpcResponse.class);
    }
}
