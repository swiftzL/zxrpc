package cn.zl.rpcclient.client;

import cn.zl.rpcclient.handler.ClientHandlerInitial;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;


/**
 * @Author: zl
 * @Date: 2021/5/10 3:37 下午
 */
public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    public NettyClient() {
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(eventLoopGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerInitial());
    }

    public Channel getChannel(ServiceDescribe serviceDescribe) {
        Channel channel = getChannel(serviceDescribe.getSocketAddress());
        if (channel == null) {
            logger.debug(serviceDescribe.getHost() + " " + serviceDescribe.getPort() + " connect fail");
        }
        return channel;
    }

    public Channel getChannel(SocketAddress socketAddress) {
        ChannelFuture connect = this.bootstrap.connect(socketAddress);
        if (connect.isSuccess()) {
            return connect.channel();
        }
        return null;
    }

}
