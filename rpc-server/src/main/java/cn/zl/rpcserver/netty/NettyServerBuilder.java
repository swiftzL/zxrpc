package cn.zl.rpcserver.netty;

import cn.zl.rpcserver.server.ServerImplBuilder;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.register.Register;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.utils.ObjectFactory;
import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

public class NettyServerBuilder extends ServerBuilderImpl<NettyServerBuilder> {

    private static ObjectFactory<EventLoopGroup> DefaultBoosEventLoopGroup;
    private static ObjectFactory<EventLoopGroup> DefaultWorkEventLoopGroup;
    private static ObjectFactory<ChannelFactory<? extends ServerChannel>> DEFAULT_CHANNEL_FACTORY;
    private static ObjectFactory<Map<ChannelOption<?>, ?>> DefaultChannelOptions;//channel profile
    private static ObjectFactory<Map<ChannelOption<?>, ?>> DefaultChannelChildOptions;
    private SocketAddress socketAddress;
    private EventLoopGroup boosEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;
    private ServerImplBuilder serverImplBuilder;
    //    private Serializer<RpcResponse> rpcResponseSerializer;
//    private Serializer<RpcRequest> rpcRequestSerializer;
//    private Register register;

    @Override
    protected ServerBuilder<?> delegate() {
        //create server builder
        if (this.serverImplBuilder == null) {
            this.serverImplBuilder = new ServerImplBuilder(socketAddress, boosEventLoopGroup, workerEventLoopGroup, DEFAULT_CHANNEL_FACTORY.getObject());
//            this.serverImplBuilder.setRegister(this.register);
        }
        return this.serverImplBuilder;
    }

    public static NettyServerBuilder forPort(int port) {
        return forAddress(new InetSocketAddress(port));
    }

    public static NettyServerBuilder forAddress(InetSocketAddress inetSocketAddress) {
        //todo something
        return new NettyServerBuilder(inetSocketAddress);
    }

    public NettyServerBuilder(SocketAddress socketAddress) {
        this(socketAddress, DefaultBoosEventLoopGroup.getObject(), DefaultWorkEventLoopGroup.getObject());
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
        DefaultBoosEventLoopGroup = ObjectFactory.getInstance(() -> Utils.createEventLoopGroup(Utils.EventLoopGroupType.NIO));
        DefaultWorkEventLoopGroup = ObjectFactory.getInstance(() -> Utils.createEventLoopGroup(Utils.EventLoopGroupType.NIO));
        DEFAULT_CHANNEL_FACTORY = ObjectFactory.getInstance(() -> Utils.createChannelFactory(Utils.EventLoopGroupType.NIO));

    }

}
