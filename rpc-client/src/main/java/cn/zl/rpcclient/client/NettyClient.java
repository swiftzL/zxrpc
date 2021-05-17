package cn.zl.rpcclient.client;

import cn.zl.rpcclient.handler.ClientHandlerInitial;
import cn.zl.rpcclient.loadbalance.RandomLoadBalancer;
import cn.zl.rpcclient.proxy.ClientProxy;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;

import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: zl
 * @Date: 2021/5/10 3:37 下午
 */
public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private static Map<Class<?>, Object> singleObjects = new HashMap<>();

    private static NettyClient nettyClient;

    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private ServiceDiscover serviceDiscover;
    private ClassLoader classLoader;
    private ChannelFactory channelFactory = Utils.createChannelFactory(Utils.EventLoopGroupType.NIO);

    public NettyClient(EventLoopGroup eventExecutors, Serializer req, Serializer resp) throws ClassNotFoundException {
//        this.eventLoopGroup = new NioEventLoopGroup();//default
        this.eventLoopGroup = eventExecutors;
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(eventLoopGroup)
                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerInitial(req, resp));
        bootstrap.channel(NioSocketChannel.class);
        nettyClient = this;
        //init channel holder
        Class.forName("cn.zl.rpcclient.client.ChannelHolder");
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    public Channel getChannel(ServiceDescribe serviceDescribe) throws InterruptedException {
        Channel channel = getChannel(serviceDescribe.getSocketAddress());
        if (channel == null) {
            logger.debug(serviceDescribe.getHost() + " " + serviceDescribe.getPort() + " connect fail");
        }
        return channel;
    }

    public Channel getChannel(SocketAddress socketAddress) throws InterruptedException {
        ChannelFuture connect = this.bootstrap.connect(socketAddress).sync();
        Channel channel = connect.channel();
        if (channel.isOpen()) {
            return channel;
        }
        return null;
    }

    public ServiceDiscover getServiceDiscover() {
        return serviceDiscover;
    }

    public static NettyClient getClient() {
        return nettyClient;
    }

    public void setServiceDiscover(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    public <T> T getService(Class<T> clazz) {
        Object o = singleObjects.get(clazz);
        if (o != null) {
            return (T) o;
        }
        ClientProxy clientProxy = new ClientProxy(clazz, serviceDiscover, new RandomLoadBalancer());
        return (T) Proxy.newProxyInstance(this.classLoader, new Class[]{clazz}, clientProxy);
    }

}
