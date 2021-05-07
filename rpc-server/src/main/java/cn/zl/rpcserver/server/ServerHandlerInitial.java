package cn.zl.rpcserver.server;

import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.handler.ProtocolJudgeDecorate;
import cn.zl.rpcserver.handler.codec.MixProtocolHandler;
import cn.zl.rpcserver.handler.codec.PrefixBaseFrameDecoder;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHandlerInitial extends ChannelInitializer<Channel> {

    private ProtocolJudgeDecorate protocolJudgeDecorate;
    private Map<String, RpcServiceMethod> methodMap;
    private Serializer<RpcResponse> rpcResponseSerializer;
    private Serializer<RpcRequest> rpcRequestSerializer;
    private Map<String, RpcServiceMethod> urlToMethodMap;

    private int maxBytes = 2048;

    public ServerHandlerInitial(List<ProtocolJudge> protocolJudges,
                                Map<String, RpcServiceMethod> methodMap,
                                Serializer<RpcResponse> rpcResponseSerializer,
                                Serializer<RpcRequest> rpcRequestSerializer,
                                Map<String, RpcServiceMethod> urlToMethodMap) {
        this.protocolJudgeDecorate = new ProtocolJudgeDecorate();
        this.methodMap = methodMap;
        protocolJudges.stream().forEach(this.protocolJudgeDecorate::register);
        this.rpcRequestSerializer = rpcRequestSerializer;
        this.rpcResponseSerializer = rpcResponseSerializer;
        this.urlToMethodMap = urlToMethodMap;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast(this.protocolJudgeDecorate.newChannelHandler());
        ch.pipeline().addLast(new MixProtocolHandler(methodMap, maxBytes, rpcResponseSerializer, rpcRequestSerializer,urlToMethodMap));
    }
}
