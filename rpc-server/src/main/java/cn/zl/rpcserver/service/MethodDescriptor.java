package cn.zl.rpcserver.service;

import cn.zl.zxrpc.rpccommon.annotation.Param;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.utils.Preconditions;
import cn.zl.zxrpc.rpccommon.utils.StringUtils;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodDescriptor {

    private String fullMethodName;
    private String[] args;
    private Method method;
    private Object object;
    private Class<?> returnType;


    public MethodDescriptor(String fullMethodName, String[] args, Object o, Method method) {
        this.method = method;
        this.returnType = method.getReturnType();
        this.object = Preconditions.checkNotNull(o);
        this.fullMethodName = Preconditions.checkNotNull(fullMethodName, "full method name is not null");
        this.args = args;

    }

    public static MethodDescriptor buildMD(Object object, Method method) {

        String methodName = method.getName();
        List<String> args = new ArrayList<>();
        Arrays.stream(method.getParameters()).forEach(parameter -> {
            //judge parameter have @Param
            Param param = parameter.getAnnotation(Param.class);
            args.add(param == null || StringUtils.isEmpty(param.value()) ? parameter.getName() : param.value());
        });
        return new MethodDescriptor(methodName, args.toArray(new String[]{}), object, method);
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public String[] getArgs() {
        return args;
    }

    public Method getMethod() {
        return method;
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}
