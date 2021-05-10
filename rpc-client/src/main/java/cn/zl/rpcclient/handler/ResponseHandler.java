package cn.zl.rpcclient.handler;

import cn.zl.zxrpc.rpccommon.internal.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: zl
 * @Date: 2021/5/10 3:37 下午
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //read bytebuf
        ByteBuf byteBuf = (ByteBuf) msg;
        byte magicByte = byteBuf.readByte();
        if (magicByte == Constant.PONG) {

            return;
        } else if (magicByte == Constant.MAGIC_NUMBER) {


        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
