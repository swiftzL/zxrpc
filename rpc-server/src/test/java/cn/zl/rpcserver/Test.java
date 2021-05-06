package cn.zl.rpcserver;

import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;

/**
 * @Author: zl
 * @Date: 2021/5/5 10:52 上午
 */
public class Test {
    public static void main(String[] args) {
        RpcRequest rpcRequest = new RpcRequest(null, "cn.zl.zxrpc.rpccommon.tmpspi.UserServiceImpl/getUser/java.lang.String//cn.zl.zxrpc.rpccommon.tmpspi.User", "123", new Object[]{"ifdsfia"});
        Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
        byte[] encode = defaultRequestSerializer.encode(rpcRequest);

        System.out.println(encode.length);


        for (int i = 0; i < encode.length; i++) {
            System.out.println(encode[i]);
        }



        //
        System.out.println("-----");

        RpcRequest decode = defaultRequestSerializer.decode(encode);
        System.out.println(decode);
    }
}
