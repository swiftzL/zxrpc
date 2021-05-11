package cn.zl.rpcclient.handler;

import cn.zl.rpcclient.client.ResponseHolder;
import cn.zl.zxrpc.rpccommon.internal.Constant;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: zl
 * @Date: 2021/5/10 3:37 下午
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter {

    private Serializer<RpcResponse> rpcResponseSerializer;

    public ResponseHandler(Serializer serializer) {
        this.rpcResponseSerializer = serializer;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //read bytebuf
        ByteBuf byteBuf = (ByteBuf) msg;

        byte magicByte = byteBuf.readByte();
        if (magicByte == Constant.PONG) {
            //handler keepAlive
            return;
        } else if (magicByte == Constant.MAGIC_NUMBER) {
            int byteLength = byteBuf.readInt();
            ByteBuf data = byteBuf.readBytes(byteLength);
            byte[] dataByte = data.array();
            RpcResponse rpcResponse = this.rpcResponseSerializer.decode(dataByte);
            String requestId = rpcResponse.getRequestId();
            ResponseHolder.completable(requestId, rpcResponse);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
