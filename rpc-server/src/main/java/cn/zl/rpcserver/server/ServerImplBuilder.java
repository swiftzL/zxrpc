package cn.zl.rpcserver.server;

import cn.zl.rpcserver.event.Event;
import cn.zl.rpcserver.netty.ServerBuilder;
import cn.zl.rpcserver.service.ServerMethodDefinition;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;

import java.net.SocketAddress;

public class ServerImplBuilder extends ServerBuilder {

    //server manager

    private SocketAddress socketAddress;
    private EventLoopGroup boosEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;
    private ChannelFactory<? extends Channel> channelFactory;

    public ServerImplBuilder(SocketAddress socketAddress, EventLoopGroup boosEventLoopGroup,
                             EventLoopGroup workerEventLoopGroup,ChannelFactory<? extends Channel> channelFactory) {
        this.socketAddress = socketAddress;
        this.boosEventLoopGroup = boosEventLoopGroup;
        this.workerEventLoopGroup = workerEventLoopGroup;
        this.channelFactory = channelFactory;
    }

    @Override
    public ServerBuilder addService(ServerMethodDefinition service) {
        return null;
    }

    @Override
    public ServerBuilder addService(Class clazz) {
        return null;
    }

    @Override
    public Server build() {
        return new NettyServer();
    }
}
