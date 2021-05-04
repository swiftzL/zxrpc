package cn.zl.rpcserver.filter;

import cn.zl.zxrpc.rpccommon.annotation.Order;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

@Order(Order.height)
public interface Filter  {

    public void init(String filterName);

    public void doFilter(ChannelHandlerContext ctx, ByteBuf byteBuf);

    public int getCurrentCounter();

    public void setNext(Filter next);
}
