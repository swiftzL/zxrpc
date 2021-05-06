package cn.zl.rpcserver.server;

import cn.zl.rpcserver.event.EventBroadCast;
import cn.zl.rpcserver.event.internalevent.ServerStartedEvent;
import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NettyServer implements Server {

    private volatile int isRunning = STOP;
    private static int RUNNING = 1;
    private static int STOP = 0;
    private SocketAddress socketAddress;
    private EventLoopGroup boosEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;
    private ChannelFactory<? extends ServerChannel> channelFactory;
    private Map<ChannelOption<?>, ?> channelOptionMap;
    private Map<ChannelOption<?>, ?> channelChildOptionMap;
    private EventBroadCast eventBroadCast = EventBroadCast.getInstance();
    private List<ProtocolJudge> protocolJudges = new LinkedList<>();

    private Map<String, RpcServiceMethod> methodMap = new HashMap<>();

    private Serializer<RpcResponse> rpcResponseSerializer;
    private Serializer<RpcRequest> rpcRequestSerializer;


    public NettyServer(SocketAddress socketAddress, EventLoopGroup boosEventLoopGroup, EventLoopGroup workerEventLoopGroup,
                       ChannelFactory<? extends ServerChannel> channelFactory,
                       Map<ChannelOption<?>, ?> channelOptionMap, Map<ChannelOption<?>, ?> channelChildOptionMap,
                       List<ProtocolJudge> protocolJudges, Map<String, RpcServiceMethod> methodMap,
                       Serializer<RpcResponse> rpcResponseSerializer,
                       Serializer<RpcRequest> rpcRequestSerializer ) {
        this.socketAddress = socketAddress;
        this.boosEventLoopGroup = boosEventLoopGroup;
        this.workerEventLoopGroup = workerEventLoopGroup;
        this.channelFactory = channelFactory;
        this.channelOptionMap = channelOptionMap;
        this.channelChildOptionMap = channelChildOptionMap;
        this.protocolJudges = protocolJudges;
        this.methodMap = methodMap;
        this.rpcRequestSerializer = rpcRequestSerializer;
        this.rpcResponseSerializer = rpcResponseSerializer;
    }


    @Override
    public boolean start() {
        if (UNSAFE.compareAndSwapInt(this, isRunningOffset, STOP, RUNNING)) {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.option();
//            serverBootstrap.childOption()
            serverBootstrap.group(this.boosEventLoopGroup, this.workerEventLoopGroup);
            serverBootstrap.channelFactory(this.channelFactory);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.localAddress(this.socketAddress);
            if (this.channelOptionMap != null) {
                for (Map.Entry<ChannelOption<?>, ?> entry : channelOptionMap.entrySet()) {
                    serverBootstrap.option((ChannelOption<Object>) entry.getKey(), entry.getValue());
                }
            }
            if (this.channelChildOptionMap != null) {
                for (Map.Entry<ChannelOption<?>, ?> entry : channelChildOptionMap.entrySet()) {
                    serverBootstrap.childOption((ChannelOption<Object>) entry.getKey(), entry.getValue());
                }
            }
            //todo ssl support ?
            serverBootstrap.childHandler(new ServerHandlerInitial(protocolJudges, methodMap,rpcResponseSerializer,rpcRequestSerializer));

            //fire server start event
            eventBroadCast.fireEvent(ServerStartedEvent.class);
            serverBootstrap.bind(this.socketAddress);
        }
        return false;
    }

    @Override
    public boolean isShutdown() {
        return this.isRunning == STOP;
    }

    @Override
    public boolean shutdown() {
        if (UNSAFE.compareAndSwapInt(this, isRunningOffset, RUNNING, STOP)) {
            return true;
        }
        return false;
    }

    @Override
    public int getPort() {
        return ((InetSocketAddress) this.socketAddress).getPort();
    }

    @Override
    public RpcServiceMethod getServiceMethod(String methodSignature) {
        return this.methodMap.get(methodSignature);
    }

    public Map<String, RpcServiceMethod> getMethodMap() {
        return methodMap;
    }

    @Override
    public SocketAddress getListenSocket() {
        return null;
    }

    private static  sun.misc.Unsafe UNSAFE;
    private static long isRunningOffset;

    static {

//        UNSAFE = sun.misc.Unsafe.getUnsafe();
        Class<?> clazz = NettyServer.class;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe"); // Internal reference
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
            isRunningOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("isRunning"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
