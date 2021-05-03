package cn.zl.rpcserver.handler;

import cn.zl.rpcserver.handler.codec.MessageDescribe;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class RequestHandler extends MessageToMessageDecoder<MessageDescribe> {


    @Override
    protected void decode(ChannelHandlerContext ctx, MessageDescribe msg, List<Object> out) throws Exception {
        //deal with http and zxrpc

    }
}
