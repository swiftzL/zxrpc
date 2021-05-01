package cn.zl.rpcserver.netty;

import cn.zl.rpcserver.server.ServerImplBuilder;
import cn.zl.zxrpc.rpccommon.utils.ObjectFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

public class NettyServerBuilder extends ServerBuilderImpl<NettyServerBuilder> {

    private static ObjectFactory<EventLoopGroup> DefaultBoosEventLoopGroup;
    private static ObjectFactory<EventLoopGroup> DefaultWorkEventLoopGroup;
    private static ObjectFactory<ChannelFactory<? extends Channel>> DEFAULT_CHANNEL_FACTORY;
    private static ObjectFactory<Map<ChannelOption<?>, ?>> DefaultChannelOptions;//channel profile
    private static ObjectFactory<Map<ChannelOption<?>,?>> DefaultChannelChildOptions;
    private SocketAddress socketAddress;
    private EventLoopGroup boosEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    @Override
    protected ServerBuilder<?> delegate() {
        //create server builder
        return new ServerImplBuilder(socketAddress,boosEventLoopGroup,workerEventLoopGroup,DEFAULT_CHANNEL_FACTORY.getObject());
    }

    public static NettyServerBuilder forPort(int port) {
        return forAddress(new InetSocketAddress(port));
    }

    public static NettyServerBuilder forAddress(InetSocketAddress inetSocketAddress) {
        //todo something
        return new NettyServerBuilder(inetSocketAddress);
    }

    public NettyServerBuilder(SocketAddress socketAddress) {
        new NettyServerBuilder(socketAddress, DefaultBoosEventLoopGroup.getObject(), DefaultWorkEventLoopGroup.getObject());
    }

    public NettyServerBuilder(SocketAddress socketAddress, EventLoopGroup boos, EventLoopGroup work) {
        this.socketAddress = socketAddress;
        this.boosEventLoopGroup = boos;
        this.workerEventLoopGroup = work;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public EventLoopGroup getBoosEventLoopGroup() {
        return boosEventLoopGroup;
    }

    public void setBoosEventLoopGroup(EventLoopGroup boosEventLoopGroup) {
        this.boosEventLoopGroup = boosEventLoopGroup;
    }

    static {
        //lazy loading
        DefaultBoosEventLoopGroup = ObjectFactory.getInstance(() -> Utils.createEventLoopGroup(Utils.EventLoopGroupType.EPOLL));
        DefaultWorkEventLoopGroup = ObjectFactory.getInstance(() -> Utils.createEventLoopGroup(Utils.EventLoopGroupType.EPOLL));
        DEFAULT_CHANNEL_FACTORY = ObjectFactory.getInstance(() -> Utils.createChannelFactory(Utils.EventLoopGroupType.EPOLL));

    }

}
