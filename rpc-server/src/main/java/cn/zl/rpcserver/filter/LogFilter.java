package cn.zl.rpcserver.filter;

import cn.zl.zxrpc.rpccommon.annotation.Order;
import cn.zl.zxrpc.rpccommon.execption.FileNotFoundException;
import cn.zl.zxrpc.rpccommon.internal.LogId;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: zl
 * @Date: 2021/5/3 2:57 下午
 */
@Order(Order.low)
public class LogFilter extends FilterAdapter {
    private String filterName;
    private LogId logId;


    @Override
    public void init(String filterName) {
        this.filterName = filterName;
        this.logId = LogId.getInstance(LogFilter.class, filterName);
    }

    @Override
    public void doFilter(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        int i = byteBuf.readableBytes();
        System.out.println(logId.toString() + "byte size is " + i);
        next.doFilter(ctx,byteBuf);
    }




    //not deal with event

}
