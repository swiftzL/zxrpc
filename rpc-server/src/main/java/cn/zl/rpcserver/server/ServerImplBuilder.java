package cn.zl.rpcserver.server;

import cn.zl.rpcserver.event.Event;
import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.netty.ServerBuilder;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.rpcserver.service.ServerMethodDefinition;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import java.net.SocketAddress;
import java.util.*;
import java.util.function.Supplier;

public class ServerImplBuilder extends ServerBuilder {

    //server manager

    private SocketAddress socketAddress;
    private EventLoopGroup boosEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;
    private ChannelFactory<? extends ServerChannel> channelFactory;
    private Serializer<RpcResponse> rpcResponseSerializer;
    private Serializer<RpcRequest> rpcRequestSerializer;
    private List<ProtocolJudge> protocolJudges = new LinkedList<>();

    private Map<String, RpcServiceMethod> methodMap = new HashMap<>();

    public ServerImplBuilder(SocketAddress socketAddress, EventLoopGroup boosEventLoopGroup,
                             EventLoopGroup workerEventLoopGroup, ChannelFactory<? extends ServerChannel> channelFactory) {
        this.socketAddress = socketAddress;
        this.boosEventLoopGroup = boosEventLoopGroup;
        this.workerEventLoopGroup = workerEventLoopGroup;
        this.channelFactory = channelFactory;
    }

    @Override
    public ServerBuilder addService(RpcServiceMethod service) {
        //add service method
        this.methodMap.put(service.methodSignature(), service);
        return this;
    }

    @Override
    public ServerBuilder addService(Class clazz, Object object) {
        if (this.rpcResponseSerializer == null || this.rpcRequestSerializer == null) {
            throw new RuntimeException("the serializer is not null");
        }
        if (object != null) {
            Arrays.stream(clazz.getDeclaredMethods()).forEach(e -> {
                addService(new RpcServiceMethod(clazz, object, e, rpcResponseSerializer, rpcRequestSerializer));
            });
        }
        return this;
    }

    @Override
    public ServerBuilder addService(Class clazz) {
        //get methods
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addService(clazz, o);
        return this;
    }

    @Override
    public ServerBuilder addProtocolJudge(ProtocolJudge protocolJudge) {
        this.protocolJudges.add(protocolJudge);
        return this;
    }

    @Override
    public ServerBuilder addSerializerResponse(Supplier rpcResponseSerializerSupplier) {
        this.rpcResponseSerializer = (Serializer<RpcResponse>) rpcResponseSerializerSupplier.get();
        return this;
    }

    @Override
    public ServerBuilder addSerializerRequest(Supplier rpcRequestSerializerSupplier) {
        this.rpcRequestSerializer = (Serializer<RpcRequest>) rpcRequestSerializerSupplier.get();
        return this;
    }

//    @Override
//    public ServerBuilder addSerializer(Supplier rpcRequestSerializerSupplier, Supplier rpcResponseSerializerSupplier) {
//        this.rpcRequestSerializer = (Serializer<RpcRequest>) rpcRequestSerializerSupplier.get();
//        this.rpcResponseSerializer = (Serializer<RpcResponse>) rpcResponseSerializerSupplier.get();
//        return this;
//    }

//    @Override
//    public ServerBuilder addSerializer(Supplier<Serializer<RpcRequest>> rpcRequestSerializerSupplier, Supplier<Serializer<RpcResponse>> rpcResponseSerializerSupplier) {
//        this.rpcRequestSerializer = rpcRequestSerializerSupplier.get();
//        this.rpcResponseSerializer = rpcResponseSerializerSupplier.get();
//        return this;
//    }

    @Override
    public Server build() {
        return new NettyServer(socketAddress, boosEventLoopGroup, workerEventLoopGroup,
                channelFactory, null, null, protocolJudges, methodMap,rpcResponseSerializer,rpcRequestSerializer);
    }
}
