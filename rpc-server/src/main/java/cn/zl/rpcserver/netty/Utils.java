package cn.zl.rpcserver.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ReflectiveChannelFactory;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ThreadFactory;

public class Utils {

    //todo : read by application class
    private static String APPLICATION_NAME = "zxrpc";

    public static enum EventLoopGroupType {
        NIO,
        EPOLL;

        private EventLoopGroupType() {
        }
    }

    public static EventLoopGroup createEventLoopGroup(EventLoopGroupType groupType){
        ThreadFactory threadFactory = new DefaultThreadFactory(APPLICATION_NAME);
        switch (groupType){
            case NIO:
                return new NioEventLoopGroup(threadFactory);
            case EPOLL:
                return new EpollEventLoopGroup(threadFactory);
            default:
                throw new RuntimeException("the eventLoopGroupType is not support");
        }
    }

    public static ChannelFactory<? extends Channel> createChannelFactory(EventLoopGroupType groupType){
        switch (groupType){
            case NIO:
                return new ReflectiveChannelFactory(NioServerSocketChannel.class);
            case EPOLL:
                return new ReflectiveChannelFactory(EpollServerSocketChannel.class);
            default:
                throw new RuntimeException("the eventLoopGroupType is not support");
        }
    }
}
