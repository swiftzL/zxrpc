package cn.zl.rpcserver.server;

import cn.zl.rpcserver.handler.codec.PrefixBaseFrameDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class ServerHandlerInitial extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
//        ch.pipeline().addLast(new PrefixBaseFrameDecoder());
    }
}
