package cn.zl.rpcserver.handler;

import cn.zl.zxrpc.rpccommon.message.HttpMethod;
import cn.zl.rpcserver.handler.codec.MessageType;
import io.netty.channel.ChannelHandler;

import java.util.HashSet;
import java.util.Set;

public class HttpProtocolJudge implements ProtocolJudge {
    @Override
    public String getProtocol() {
        return "http";
    }


    @Override
    public ChannelHandler newChannelHandler() {
        return null;
    }

    @Override
    public Set<String> getPrefix() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add(HttpMethod.GET);
        prefixes.add(HttpMethod.POST);
        return prefixes;

    }

    @Override
    public MessageType messageType() {
        return MessageType.HTTP;
    }
}
