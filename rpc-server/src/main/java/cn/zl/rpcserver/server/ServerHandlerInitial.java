package cn.zl.rpcserver.server;

import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.handler.ProtocolJudgeDecorate;
import cn.zl.rpcserver.handler.codec.MixProtocolHandler;
import cn.zl.rpcserver.handler.codec.PrefixBaseFrameDecoder;
import cn.zl.rpcserver.service.RpcServiceMethod;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHandlerInitial extends ChannelInitializer<Channel> {

    private ProtocolJudgeDecorate protocolJudgeDecorate;
    private Map<String, RpcServiceMethod> methodMap = new HashMap<>();

    public ServerHandlerInitial(List<ProtocolJudge> protocolJudges, Map<String, RpcServiceMethod> methodMap) {
        this.protocolJudgeDecorate = new ProtocolJudgeDecorate();
        this.methodMap = methodMap;
        protocolJudges.stream().forEach(this.protocolJudgeDecorate::register);
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast(this.protocolJudgeDecorate.newChannelHandler());
        ch.pipeline().addLast(new MixProtocolHandler());
    }
}
