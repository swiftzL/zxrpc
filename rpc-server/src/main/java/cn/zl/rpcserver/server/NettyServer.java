package cn.zl.rpcserver.server;

import cn.zl.rpcserver.event.EventBroadCast;
import cn.zl.rpcserver.event.internalevent.ServerStartedEvent;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
            serverBootstrap.handler(new ServerHandlerInitial());


            //fire server start event
            eventBroadCast.fireEvent(ServerStartedEvent.class);
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
        return ((InetSocketAddress)this.socketAddress).getPort();
    }

    @Override
    public SocketAddress getListenSocket() {
        return null;
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static long isRunningOffset;

    static {
        UNSAFE = sun.misc.Unsafe.getUnsafe();
        Class<?> clazz = NettyServer.class;
        try {
            isRunningOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("isRunning"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
