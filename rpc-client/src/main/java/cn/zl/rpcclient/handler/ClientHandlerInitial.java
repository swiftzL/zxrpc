package cn.zl.rpcclient.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @Author: zl
 * @Date: 2021/5/10 5:36 下午
 */
public class ClientHandlerInitial extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new RequestEncoder());
        ch.pipeline().addLast(new ResponseHandler());
    }
}
