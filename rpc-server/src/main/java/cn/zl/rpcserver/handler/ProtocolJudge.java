package cn.zl.rpcserver.handler;


import io.netty.channel.ChannelHandler;

// 协议判断
public interface ProtocolJudge  {

    String getProtocol();

    ChannelHandler newChannelHandler();
}
