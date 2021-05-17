package cn.zl.rpcclient.proxy;

import cn.zl.rpcclient.client.ChannelHolder;
import cn.zl.rpcclient.client.ResponseHolder;
import cn.zl.rpcclient.loadbalance.LoadBalancer;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: zl
 * @Date: 2021/5/15 2:42 下午
 */
public class ClientProxy implements InvocationHandler {

    private static AtomicLong atomicLong = new AtomicLong();


    private Class<?> interfaceClass;
    private Map<Method, String> methodToSignature;
    private String serviceName;
    private ServiceDiscover serviceDiscover;
    private LoadBalancer loadBalancer;


    public ClientProxy(Class<?> interfaceClass, ServiceDiscover serviceDiscover, LoadBalancer loadBalancer) {
        this.methodToSignature = new ConcurrentHashMap<>();
        this.interfaceClass = interfaceClass;
        this.serviceName = this.interfaceClass.getName();
        this.serviceDiscover = serviceDiscover;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //get method
        String signature = this.methodSignature(method);

        List<ServiceDescribe> service = serviceDiscover.getService(serviceName);
        if (service == null || service.size() == 0) {
            return null;
        }
        ServiceDescribe select = loadBalancer.select(service);
        Channel channel = ChannelHolder.getChannel(select);
        long next = atomicLong.getAndIncrement();
        String nextId = String.valueOf(next);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setArgs(args);
        rpcRequest.setRequestId(nextId);
        rpcRequest.setUrl(signature);
        if (channel.isWritable()) {//防止发送消息积压
            ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest);
            CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
            ResponseHolder.put(nextId, completableFuture);
            RpcResponse rpcResponse = completableFuture.get();
            return rpcResponse.getData();
        } else {
            return null;
        }

//        System.out.println(rpcResponse);

    }


    public String methodSignature(Method method) {
        String signature = methodToSignature.get(method);
        if (signature != null) {
            return signature;
        }
        String className = this.interfaceClass.getName();
//        Method method = this.serverMethodDefinition.getMethodDescriptor().getMethod();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameters = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            parameters.append(clazz.getName() + "/");
        }
        String result = className + "/" + methodName + "/" + parameters.toString() + "/" +
                method.getReturnType().getName();
        this.methodToSignature.put(method, result);
        return result;
    }


}
