package cn.zl.rpcserver.handler.codec;

import io.netty.buffer.ByteBuf;

public class MessageDescribe {

    private MessageType messageType;
    private ByteBuf body;

    public MessageDescribe(){

    }

    public MessageDescribe(MessageType messageType, ByteBuf body) {
        this.messageType = messageType;
        this.body = body;
    }
}
