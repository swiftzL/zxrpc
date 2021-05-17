package cn.zl.rpcclient.handler;

import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: zl
 * @Date: 2021/5/10 5:36 下午
 */
public class ClientHandlerInitial extends ChannelInitializer<SocketChannel> {
    private Serializer requestSerializer;
    private Serializer<RpcResponse> rpcResponseSerializer;

    public ClientHandlerInitial(Serializer requestSerializer, Serializer<RpcResponse> rpcResponseSerializer) {
        this.requestSerializer = requestSerializer;
        this.rpcResponseSerializer = rpcResponseSerializer;
    }

    @Override
    protected void initChannel(SocketChannel ch)  {
        ch.pipeline().addLast(new ResponseHandler(this.rpcResponseSerializer));
        ch.pipeline().addLast(new RequestEncoder(this.requestSerializer));
    }
}
