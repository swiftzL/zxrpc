package cn.zl.rpcserver.netty;

import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;

public class NettyServerBuilder extends ServerBuilderImpl<NettyServerBuilder>{

    private EventLoopGroup BoosEventLoopGroup;
    private EventLoopGroup WorkEventLoopGroup;


    @Override
    protected ServerBuilder<?> delegate() {
        return null;
    }

    public static NettyServerBuilder forport(int port){
        return forAddress(new InetSocketAddress(port));
    }
    public static NettyServerBuilder forAddress(InetSocketAddress inetSocketAddress){
        //todo something
        return null;
    }

}
