package cn.zl.rpcserver.handler;

import cn.zl.rpcserver.handler.codec.MessageType;
import io.netty.channel.ChannelHandler;

import java.util.HashSet;
import java.util.Set;

public class ZxprcProtocolJudge implements ProtocolJudge {
    @Override
    public String getProtocol() {
        return "zxrpc";
    }

    @Override
    public ChannelHandler newChannelHandler() {
        return null;
    }

    @Override
    public Set<String> getPrefix() {
        Set<String> s = new HashSet<>();
        s.add("zxrpc");
        return s;
    }

    @Override
    public MessageType messageType() {
        return MessageType.ZXRPC;
    }
}
