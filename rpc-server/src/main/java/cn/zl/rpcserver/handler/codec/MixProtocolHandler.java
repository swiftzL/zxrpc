package cn.zl.rpcserver.handler.codec;

import cn.zl.rpcserver.service.RpcServiceMethod;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/4 11:09 上午
 */
public class MixProtocolHandler extends MessageToMessageDecoder<MessageDescribe> {

    private Map<String, RpcServiceMethod> urlToInvoke;

    private int maxBytes;

    public MixProtocolHandler(){

    }

    public MixProtocolHandler(Map<String, RpcServiceMethod> urlToInvoke, int maxBytes) {
        this.urlToInvoke = urlToInvoke;
        this.maxBytes = maxBytes;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, MessageDescribe msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.getBody();
        if (msg.getMessageType() == MessageType.ZXRPC) {
            //get url
            byteBuf.skipBytes(5);
            int urlLen = byteBuf.readInt();
            ByteBuf urlByteBuf = byteBuf.readBytes(urlLen);
            String url = urlByteBuf.toString(Charset.defaultCharset());
            //analyze url


            //get serializeBytes
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes > this.maxBytes) {
                ctx.fireExceptionCaught(new RuntimeException("this bytes is too long-->" + byteBuf.readableBytes()));
            }
            byte[] serializeBytes = new byte[this.maxBytes];
            byteBuf.readBytes(serializeBytes, 0, readableBytes - 4);

            byte[] serializeBytesResponse = urlToInvoke.get(url).getResponseBytes(serializeBytes);
            ctx.write(Unpooled.copiedBuffer(serializeBytesResponse));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
