package cn.zl.rpcserver.filter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: zl
 * @Date: 2021/5/3 8:19 下午
 */
public abstract class FilterAdapter implements Filter {

    protected Filter next;

    protected Integer counter;

    public void executeFilter(ChannelHandlerContext ctx, ByteBuf byteBuf){
        //add
        doFilter(ctx,byteBuf);
        //sub

    }
    public int getCurrentCounter(){
        int count = this.counter;
        this.counter = 0;
        return count;
    }

    @Override
    public void setNext(Filter next) {
        this.next = next;
    }
}
