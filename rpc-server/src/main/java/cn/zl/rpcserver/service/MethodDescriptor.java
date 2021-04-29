package cn.zl.rpcserver.service;

import cn.zl.zxrpc.rpccommon.annotation.Param;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.utils.Preconditions;
import cn.zl.zxrpc.rpccommon.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodDescriptor<Req,Resp> {

    private String fullMethodName;
    private String[] args;
    private  Serializer<Req> reqSerializer;
    private Serializer<Resp> respSerializer;

    public MethodDescriptor(String fullMethodName, String[] args, Serializer<Req> reqSerializer, Serializer<Resp> respSerializer) {
        this.fullMethodName = Preconditions.checkNotNull(fullMethodName,"fullmethodname is not null");
        this.args = args;
        this.reqSerializer = reqSerializer;
        this.respSerializer = respSerializer;
    }

    public static <Req,Resp> MethodDescriptor<Req,Resp> buildMD(Method method,Serializer<Req> reqSerializer,Serializer<Resp> respSerializer) {
        String methodName = method.getName();
        List<String> args = new ArrayList<>();
        Arrays.stream(method.getParameters()).forEach(parameter -> {
            //judge parameter have @Param
            Param param = parameter.getAnnotation(Param.class);
            args.add(param == null || StringUtils.isEmpty(param.value()) ? parameter.getName() : param.value());
        });
        return new MethodDescriptor<>(methodName, args.toArray(new String[]{}), reqSerializer, respSerializer);

    }
}
