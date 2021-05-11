package cn.zl.rpcclient;

import cn.zl.rpcclient.client.ClientBuilder;
import cn.zl.rpcclient.client.NettyClient;
import cn.zl.rpcclient.client.Utils;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdServiceDiscover;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;

/**
 * @Author: zl
 * @Date: 2021/5/10 10:19 下午
 */
public class TestClient {
    public static void main(String[] args) {
        //get register server
        NettyClient client = ClientBuilder.newBuilder().eventExecutors(Utils.createEventLoopGroup(Utils.EventLoopGroupType.NIO))
                .requestSerializer(SerializerHelper.getDefaultRequestSerializer()).serviceDiscover(new EtcdServiceDiscover())
                .build();
        client.getServiceDiscover().getService("d");



    }
}
