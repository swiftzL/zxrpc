package cn.zl.rpcserver.handler;

import io.netty.channel.Channel;

public class NettyServerHandler {


    private Channel channel;

    private NettyServerHandler(Channel channel){
        this.channel = channel;
    }

    public static NettyServerHandler newHandler(Channel channel){
        return new NettyServerHandler(channel);
    }
}
