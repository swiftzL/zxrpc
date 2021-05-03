package cn.zl.rpcserver.handler;


import cn.zl.rpcserver.handler.codec.MessageType;
import io.netty.channel.ChannelHandler;

import java.util.Set;

// 协议判断
public interface ProtocolJudge  {

    String getProtocol();

    ChannelHandler newChannelHandler();

    Set<String> getPrefix();

    MessageType messageType();
}
