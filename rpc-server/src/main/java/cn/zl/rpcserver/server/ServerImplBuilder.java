package cn.zl.rpcserver.server;

import cn.zl.rpcserver.event.Event;
import cn.zl.rpcserver.handler.ProtocolJudge;
import cn.zl.rpcserver.handler.codec.HttpUtils;
import cn.zl.rpcserver.handler.restful.RequestTree;
import cn.zl.rpcserver.netty.ServerBuilder;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.rpcserver.service.ServerMethodDefinition;
import cn.zl.zxrpc.rpccommon.annotation.Controller;
import cn.zl.zxrpc.rpccommon.annotation.RequestMapping;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
    private Map<String, RpcServiceMethod> urlToMethodMap = new HashMap<>();
    private RequestTree requestTree = RequestTree.getInstance();


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

    public ServerBuilder addService(RpcServiceMethod service, String urlPrefix) {
        if (urlPrefix != null) {
            if (urlPrefix.endsWith("/")) { //if url prefix is /
                urlPrefix = "";
            }
            Method method = service.getServerMethodDefinition().getMethodDescriptor().getMethod();
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {

                String urlPostfix = requestMapping.value();
                urlPostfix = urlPrefix.startsWith("/") ? urlPostfix : "/" + urlPostfix;
                urlPostfix = urlPrefix.endsWith("/") ? urlPostfix.substring(0, urlPostfix.length() - 1) : urlPostfix;
                String fullUrl = urlPrefix + urlPostfix;
                if (fullUrl.indexOf(":") == -1) {
                    this.urlToMethodMap.put(fullUrl, service);
                } else {//restful url  /user/:id/:url  0 1
                    String[] splitUrl = HttpUtils.splitUrl(fullUrl);
                    StringBuilder actualUrl = new StringBuilder();
                    int argsNumber = 0;
                    for (String url : splitUrl) {
                        if (!url.startsWith(":")) {
                            actualUrl.append("/" + url);
                        } else {
                            argsNumber++;
                        }
                    }
                    if (argsNumber > 0) {
                        this.requestTree.addRoute(actualUrl.toString(), argsNumber, service);
                    }

                }

            }
        }
        addService(service);
        return this;
    }

    @Override
    public ServerBuilder addService(Class clazz, Object object) {
        if (this.rpcResponseSerializer == null || this.rpcRequestSerializer == null) {
            throw new RuntimeException("the serializer is not null");
        }
        //http
        Controller controller = (Controller) clazz.getAnnotation(Controller.class);
        String urlPrefix = null;
        if (controller != null) {
            urlPrefix = controller.value();
            if (urlPrefix != null && !StringUtils.isEmpty(urlPrefix)) {
                urlPrefix = urlPrefix.startsWith("/") ? urlPrefix : "/" + urlPrefix;
            }
        }

        if (object != null ) {
            String finalUrlPrefix1 = urlPrefix;
            Arrays.stream(clazz.getDeclaredMethods()).forEach(e -> {
                addService(new RpcServiceMethod(clazz, object, e, rpcResponseSerializer, rpcRequestSerializer), finalUrlPrefix1);
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
        NettyServer nettyServer = new NettyServer(socketAddress, boosEventLoopGroup, workerEventLoopGroup,
                channelFactory, null, null, protocolJudges,
                methodMap, rpcResponseSerializer, rpcRequestSerializer, urlToMethodMap);
        nettyServer.setRequestTree(requestTree);
        return nettyServer;
    }
}
