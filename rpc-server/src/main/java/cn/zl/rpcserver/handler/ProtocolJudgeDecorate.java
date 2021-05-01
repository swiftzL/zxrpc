package cn.zl.rpcserver.handler;

import io.netty.channel.ChannelHandler;

import java.util.Map;

public class ProtocolJudgeDecorate implements ProtocolJudge {


    private Map<String,ProtocolJudge> protocolJudgeMap;

    @Override
    public String getProtocol() {
        return this.protocolJudgeMap.toString();
    }

    //protocol not found
    @Override
    public ChannelHandler newChannelHandler() {
        return null;
    }
}
