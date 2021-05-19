package cn.zl.rpcserver;

import cn.zl.rpcserver.handler.HttpProtocolJudge;
import cn.zl.rpcserver.handler.ZxprcProtocolJudge;
import cn.zl.rpcserver.netty.ServerBuilder;
import cn.zl.rpcserver.server.NettyServer;
import cn.zl.rpcserver.server.Server;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.register.Register;
import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdRegister;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import cn.zl.zxrpc.rpccommon.tmpspi.User;
import cn.zl.zxrpc.rpccommon.tmpspi.UserServiceImpl;

import java.util.List;

/**
 * @Author: zl
 * @Date: 2021/5/4 2:47 下午
 */
public class TestServer1 {

    static byte[] b = new byte[]{1,1,24,13,1,0,1,102,100,97,-13,1,-127,0};
    public static void main(String[] args)throws Exception {

        List<RegisterServer> registerServers = RegisterServer.newList("http://", "192.168.31.101");
        Register etcdRegister = new EtcdRegister(registerServers);
//        NettyServerBuilder serverBuilder = new NettyServerBuilder(new InetSocketAddress(8080));
//        System.out.println(serverBuilder.getBoosEventLoopGroup());

//        Serializer defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
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
//        RpcResponse decode2 = defaultResponseSerializer.decode(b);
//        System.out.println(decode2);


        Server server = ServerBuilder.forPort(8080).addSerializerRequest(SerializerHelper::getDefaultRequestSerializer)
                .register(etcdRegister)
                .addSerializerResponse(SerializerHelper::getDefaultResponseSerializer)
//                .addService(Service.class,new Service())
                .addService(UserServiceImpl.class,new UserServiceImpl())
                .addProtocolJudge(new ZxprcProtocolJudge()).addProtocolJudge(new HttpProtocolJudge()).build();


        System.out.println(server.getPort());
        NettyServer nettyServer = (NettyServer)server;
        RpcServiceMethod serviceMethod = nettyServer.getServiceMethod("cn.zl.rpcserver.enventtest.Service/get/java.lang.String//java.lang.String");
        System.out.println(serviceMethod);
        System.out.println(nettyServer.getMethodMap());
//        RpcResponse test = serviceMethod.invoke("test");
//        System.out.println(test);

        server.start();
    }
}
