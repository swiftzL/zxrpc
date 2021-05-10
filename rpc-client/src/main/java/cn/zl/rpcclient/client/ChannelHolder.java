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
    private static Map<ServiceDescribe, Channel> serviceToChannel = new ConcurrentHashMap<>();


    public static Channel getChannel(ServiceDescribe serviceDescribe) {
        return serviceToChannel.get(serviceDescribe);
    }

    public static void set(ServiceDescribe serviceDescribe, Channel channel) {
        serviceToChannel.put(serviceDescribe, channel);
    }

}
