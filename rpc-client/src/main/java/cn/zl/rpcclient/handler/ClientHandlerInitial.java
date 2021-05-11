package cn.zl.rpcclient.handler;

import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @Author: zl
 * @Date: 2021/5/10 5:36 下午
 */
public class ClientHandlerInitial extends ChannelInitializer {
    private Serializer requestSerializer;
    private Serializer<RpcResponse> rpcResponseSerializer;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new RequestEncoder(requestSerializer));
        ch.pipeline().addLast(new ResponseHandler(rpcResponseSerializer));
    }
}
