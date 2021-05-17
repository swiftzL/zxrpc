package cn.zl.rpcserver;

import cn.zl.rpcserver.handler.HttpProtocolJudge;
import cn.zl.rpcserver.handler.ZxprcProtocolJudge;
import cn.zl.rpcserver.netty.ServerBuilder;
import cn.zl.rpcserver.server.Server;
import cn.zl.zxrpc.rpccommon.message.Header;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.register.Register;
import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdRegister;
import cn.zl.zxrpc.rpccommon.serializer.RpcSerializer;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import cn.zl.zxrpc.rpccommon.serializer.kryo.KryoBuilder;
import cn.zl.zxrpc.rpccommon.tmpspi.User;
import cn.zl.zxrpc.rpccommon.tmpspi.UserServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: zl
 * @Date: 2021/5/16 11:53 上午
 */
public class TestKryo {

    static Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
    static Serializer<RpcResponse> responseSerializer = SerializerHelper.getDefaultResponseSerializer();
    static byte[] b = new byte[]{1,1,24,13,1,0,1,102,100,97,-13,1,-127,0};
    public static void test(){
//        RpcRequest rpcRequest = new RpcRequest(null, "cn.zl.zxrpc.rpccommon.tmpspi.UserService/getUser/java.lang.String//cn.zl.zxrpc.rpccommon.tmpspi.User", "123", new Object[]{"rewrw"});
//
//        byte[] encode = defaultRequestSerializer.encode(rpcRequest);
//        RpcRequest decode = defaultRequestSerializer.decode(encode);
//        System.out.println(decode+"--->");

        RpcRequest rpcRequest = new RpcRequest(null, "cn.zl.zxrpc.rpccommon.tmpspi.UserService/getUser/java.lang.String//cn.zl.zxrpc.rpccommon.tmpspi.User", "123", new Object[]{"rewrw"});
        Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
        byte[] encode = defaultRequestSerializer.encode(rpcRequest);
        RpcRequest decode = defaultRequestSerializer.decode(encode);
        System.out.println(decode+"--->");

        Serializer<RpcResponse> defaultResponseSerializer = SerializerHelper.getDefaultResponseSerializer();
        byte[] file_responses = defaultResponseSerializer.encode(RpcResponse.success("123",new User("fjsadfa")));
        Object decode1 = defaultResponseSerializer.decode(file_responses);
        System.out.println(decode1+"--->fail response");


        System.out.println("-------");
        RpcResponse decode2 = defaultResponseSerializer.decode(b);
        System.out.println(decode2);

        RpcResponse decode3 = responseSerializer.decode(b);
        System.out.println(decode3);
    }

    public static void test2() throws InterruptedException, ExecutionException, TimeoutException, IOException {
//        List<RegisterServer> registerServers = RegisterServer.newList("http://", "192.168.31.101");
//        Register etcdRegister = new EtcdRegister(registerServers);
        RpcRequest rpcRequest = new RpcRequest(null, "cn.zl.zxrpc.rpccommon.tmpspi.UserService/getUser/java.lang.String//cn.zl.zxrpc.rpccommon.tmpspi.User", "123", new Object[]{"rewrw"});
        Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
        byte[] encode = defaultRequestSerializer.encode(rpcRequest);
        RpcRequest decode = defaultRequestSerializer.decode(encode);
        System.out.println(decode+"--->");


        Serializer<RpcResponse> defaultResponseSerializer = SerializerHelper.getDefaultResponseSerializer();
        byte[] file_responses = defaultResponseSerializer.encode(RpcResponse.success("123",new User("fjsadfa")));
        System.out.println(file_responses.length);

        Object decode1 = defaultResponseSerializer.decode(file_responses);
        System.out.println(decode1+"--->fail response");




//        System.out.println("-------");
//        RpcResponse decode2 = defaultResponseSerializer.decode(b);
//        System.out.println(decode2);

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException, IOException {
//        for(int i=0;i<100;i++){
//            new Thread(()->test()).start();
//
//        }
        test2();

    }
}
