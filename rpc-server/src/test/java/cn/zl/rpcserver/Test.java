package cn.zl.rpcserver;

import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;

import java.nio.charset.Charset;

/**
 * @Author: zl
 * @Date: 2021/5/5 10:52 上午
 */
public class Test {
    public static void main123(String[] args) {
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

    public static void main(String[] args) {
        int i =0;
        int j=3;
        int v=5;
        int x=6;
        String t = new String("kfjsalf");
        byte[] bytes = t.getBytes();
        System.out.println(new String(bytes,0,4, Charset.defaultCharset()));
        i=j=v=x=0;
        System.out.println(i);
        System.out.println(j);
        System.out.println(v);
        System.out.println(x);
    }
}
