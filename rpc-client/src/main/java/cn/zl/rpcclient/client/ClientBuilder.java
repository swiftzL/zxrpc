package cn.zl.rpcclient.client;

import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.channel.EventLoopGroup;

/**
 * @Author: zl
 * @Date: 2021/5/10 5:48 下午
 */
public class ClientBuilder {

    private ServiceDiscover serviceDiscover;
    private Serializer requestSerializer;
    private Serializer responseSerializer;
    private EventLoopGroup eventExecutors;


    public ClientBuilder serviceDiscover(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
        return this;
    }

    public ClientBuilder eventExecutors(EventLoopGroup eventExecutors) {
        this.eventExecutors = eventExecutors;
        return this;
    }

    public ClientBuilder requestSerializer(Serializer serializer) {
        this.requestSerializer = serializer;
        return this;
    }
    public ClientBuilder responseSerializer(Serializer serializer){
        this.responseSerializer = serializer;
        return this;
    }


    public NettyClient build() throws ClassNotFoundException {
        NettyClient nettyClient = new NettyClient(this.eventExecutors,requestSerializer,responseSerializer);
        nettyClient.setServiceDiscover(this.serviceDiscover);
        return nettyClient;
    }

    public static ClientBuilder newBuilder() {
        return new ClientBuilder();
    }

}
