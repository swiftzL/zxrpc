package cn.zl.rpcserver.filter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * @Author: zl
 * @Date: 2021/5/4 10:55 上午
 */
public class FilterHandler extends ChannelInboundHandlerAdapter {

//    private List<Filter> filterList;

    private Filter firstFilter;

    private int filterSize;



    public FilterHandler(List<Filter> filterList) {
        if (filterList == null || filterList.size() == 0) {
            return;
        }
        this.filterSize = filterList.size();
        this.firstFilter = filterList.get(0);
        for (int i = 0; i < filterList.size() - 1; i++) {
            filterList.get(i).setNext(filterList.get(i + 1));
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        firstFilter.doFilter(ctx, (ByteBuf) msg);
        if (firstFilter.getCurrentCounter() == filterSize) {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
