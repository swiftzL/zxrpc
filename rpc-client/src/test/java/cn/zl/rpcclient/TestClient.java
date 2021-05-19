package cn.zl.rpcclient;

import cn.zl.rpcclient.client.ClientBuilder;
import cn.zl.rpcclient.client.NettyClient;
import cn.zl.rpcclient.client.Utils;
import cn.zl.zxrpc.rpccommon.register.Register;
import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdRegister;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdServiceDiscover;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import cn.zl.zxrpc.rpccommon.tmpspi.User;
import cn.zl.zxrpc.rpccommon.tmpspi.UserService;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: zl
 * @Date: 2021/5/10 10:19 下午
 */
public class TestClient {
    public static void main(String[] args) throws ClassNotFoundException, InterruptedException, ExecutionException, TimeoutException, IOException {
        //get register server
        List<RegisterServer> registerServers = RegisterServer.newList("http://", "192.168.31.101");
//        Register etcdRegister = new EtcdRegister(registerServers);
//        etcdRegister.doRegister("cn.zl.service");

        ServiceDiscover instance = EtcdServiceDiscover.getInstance(registerServers);

        NettyClient client = ClientBuilder.newBuilder().eventExecutors(Utils.createEventLoopGroup(Utils.EventLoopGroupType.NIO))
                .requestSerializer(SerializerHelper.getDefaultRequestSerializer())
                .responseSerializer(SerializerHelper.getDefaultResponseSerializer()).serviceDiscover(instance)
                .build();
//        client.getServiceDiscover().getService("");
        UserService service = client.getService(UserService.class);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(()->{
                User d = service.getUser("zlz", finalI);
                System.out.println(d);
            }).start();
        }


//        Proxy.newProxyInstance()


    }
}
