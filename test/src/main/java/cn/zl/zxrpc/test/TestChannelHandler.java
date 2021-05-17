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

    private byte[] b = new byte[]{1,1,24,13,1,0,1,102,100,97,-13,1,-127,0};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.get();
        Serializer<RpcResponse> defaultResponseSerializer = SerializerHelper.getDefaultResponseSerializer();

        ByteBuf byteBuf = (ByteBuf)msg;
        byteBuf.readInt();
        int len = byteBuf.readInt();
        System.out.println(len);
        byte[] bytes = new byte[len];

        System.out.println(byteBuf.readableBytes());
        byteBuf.readBytes(bytes);

        for(int i=0;i<len;i++){
            System.out.println(bytes[i]);
        }

        byte[] zls = defaultResponseSerializer.encode(RpcResponse.success("1234", new User("zl")));
        RpcResponse decode1 = defaultResponseSerializer.decode(zls);
        System.out.println(decode1);
        System.out.println("--------");



        RpcResponse<User> decode = defaultResponseSerializer.decode(bytes);
//        System.out.println(decode.getData().getName());
        System.out.println(decode.getData());
        super.channelRead(ctx, msg);

    }
}
