package cn.zl.rpcclient.handler;

import cn.zl.zxrpc.rpccommon.internal.Constant;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: zl
 * @Date: 2021/5/10 4:11 下午
 */
public class RequestEncoder extends MessageToByteEncoder<RpcRequest> {
    private Serializer serializer;

    public RequestEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(Constant.PROTOCOL_TYPE, Constant.UTF_8);
//        ByteBufUtil.hexDump(byteBuf);
        byte[] encode = serializer.encode(msg);

        byteBuf.writeBytes(encode);
        byteBuf.writeCharSequence(Constant.DELIMITER, Constant.UTF_8);// /r/n/r/n
        ByteBufUtil.hexDump(byteBuf);
        out.writeBytes(byteBuf);
    }
}
