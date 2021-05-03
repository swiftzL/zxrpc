package cn.zl.rpcserver.handler;

import cn.zl.rpcserver.handler.codec.MessageType;
import cn.zl.rpcserver.handler.codec.PrefixBaseFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProtocolJudgeDecorate implements ProtocolJudge {


    private Map<MessageType, ProtocolJudge> protocolJudgeMap;

    public ProtocolJudgeDecorate() {
        this.protocolJudgeMap = new HashMap<>();
    }

    private ProtocolJudgeDecorate register(ProtocolJudge protocolJudge) {
        this.protocolJudgeMap.put(protocolJudge.messageType(), protocolJudge);
        return this;
    }

    @Override
    public String getProtocol() {
        return this.protocolJudgeMap.toString();
    }

    //protocol not found
    @Override
    public ChannelHandler newChannelHandler() {
        return new PrefixBaseFrameDecoder(this);
    }

    @Override
    public Set<String> getPrefix() {
        Set<String> prefixes = new HashSet<>();
        this.protocolJudgeMap.values().forEach(e -> {
            prefixes.addAll(e.getPrefix());
        });
        return prefixes;
    }

    @Override
    public MessageType messageType() {
        return null;
    }

    public Map<MessageType, Set<ByteBuf>> getProtocolToByteBuf() {
        Map<MessageType, Set<ByteBuf>> map = new HashMap<>();
        for (MessageType messageType : this.protocolJudgeMap.keySet()) {
            Set<ByteBuf> byteBufs = map.get(messageType);
            if (byteBufs == null) {
                byteBufs = new HashSet<>();
            }
            Set<ByteBuf> finalByteBufs = byteBufs;
            this.protocolJudgeMap.get(messageType).getPrefix().stream().forEach(e -> {
                ByteBuf byteBuf = Unpooled.copiedBuffer(e, Charset.defaultCharset());
                finalByteBufs.add(byteBuf);
            });

            map.put(messageType, byteBufs);
        }
        return map;
    }


}
