package cn.zl.rpcserver;

import cn.zl.rpcserver.enventtest.Service;
import cn.zl.rpcserver.handler.HttpProtocolJudge;
import cn.zl.rpcserver.handler.ZxprcProtocolJudge;
import cn.zl.rpcserver.netty.NettyServerBuilder;
import cn.zl.rpcserver.netty.ServerBuilder;
import cn.zl.rpcserver.server.NettyServer;
import cn.zl.rpcserver.server.Server;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import cn.zl.zxrpc.rpccommon.tmpspi.UserService;
import cn.zl.zxrpc.rpccommon.tmpspi.UserServiceImpl;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

/**
 * @Author: zl
 * @Date: 2021/5/4 2:47 下午
 */
public class TestServer {

    public static void main(String[] args)throws Exception {
//        NettyServerBuilder serverBuilder = new NettyServerBuilder(new InetSocketAddress(8080));
//        System.out.println(serverBuilder.getBoosEventLoopGroup());
        Server server = ServerBuilder.forPort(8080).addSerializerRequest(SerializerHelper::getDefaultRequestSerializer)
                .addSerializerResponse(SerializerHelper::getDefaultResponseSerializer)
                .addService(Service.class,new Service())
                .addService(UserServiceImpl.class,new UserServiceImpl())
                .addProtocolJudge(new ZxprcProtocolJudge()).addProtocolJudge(new HttpProtocolJudge()).build();


        System.out.println(server.getPort());
        NettyServer nettyServer = (NettyServer)server;
        RpcServiceMethod serviceMethod = nettyServer.getServiceMethod("cn.zl.rpcserver.enventtest.Service/get/java.lang.String//java.lang.String");
        System.out.println(nettyServer.getMethodMap());
        RpcResponse test = serviceMethod.invoke("test");
        System.out.println(test);
        server.start();
    }
}
