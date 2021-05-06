package cn.zl.zxrpc.test;

import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import cn.zl.zxrpc.rpccommon.tmpspi.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: zl
 * @Date: 2021/5/4 9:00 下午
 */
public class TestChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Serializer<RpcResponse> defaultRequestSerializer = SerializerHelper.getDefaultResponseSerializer();
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        System.out.println(byteBuf.readableBytes());
        byteBuf.readBytes(bytes);
        RpcResponse<User> decode = defaultRequestSerializer.decode(bytes);
//        System.out.println(decode.getData().getName());
        System.out.println(decode);
        super.channelRead(ctx, msg);

    }
}
