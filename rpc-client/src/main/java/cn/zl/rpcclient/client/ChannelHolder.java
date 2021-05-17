package cn.zl.rpcclient.client;

import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zl
 * @Date: 2021/5/10 5:09 下午
 */
public class ChannelHolder {
    private static volatile Map<ServiceDescribe, Channel> serviceToChannel = new ConcurrentHashMap<>();

    private static volatile Map<Channel, ServiceDescribe> channelToService = new ConcurrentHashMap<>();

    private static NettyClient nettyClient = NettyClient.getClient();

    private static Object lock = new Object();

    public static Channel getChannel(ServiceDescribe serviceDescribe) throws InterruptedException {
        Channel channel = serviceToChannel.get(serviceDescribe);
        if (channel != null && channel.isActive()) {
            return channel;
        } else {
            synchronized (lock) {
                channel = serviceToChannel.get(serviceDescribe);
                if (channel != null) {
                    return channel;
                }
                channel = nettyClient.getChannel(serviceDescribe);
                if (channel != null) {
                    set(serviceDescribe, channel);
                }
                return channel;
            }
        }
    }

    public static void set(ServiceDescribe serviceDescribe, Channel channel) {
        serviceToChannel.put(serviceDescribe, channel);
        channelToService.put(channel, serviceDescribe);
    }
    //thread


}
