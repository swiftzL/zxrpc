package cn.zl.rpcserver.filter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Filter  {

    public void init(String filterName);

    public void doFilter(ChannelHandlerContext ctx, ByteBuf byteBuf);
}
