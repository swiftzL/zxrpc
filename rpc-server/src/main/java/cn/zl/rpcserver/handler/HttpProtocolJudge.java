package cn.zl.rpcserver.handler;

import io.netty.channel.ChannelHandler;

public class HttpProtocolJudge implements ProtocolJudge {
    @Override
    public String getProtocol() {
        return "http";
    }

    @Override
    public ChannelHandler newChannelHandler() {
        return null;
    }
}
