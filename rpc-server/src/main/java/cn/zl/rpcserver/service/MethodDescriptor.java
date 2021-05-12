package cn.zl.rpcserver.service;

import cn.zl.rpcserver.handler.codec.HttpUtils;
import cn.zl.zxrpc.rpccommon.annotation.Param;
import cn.zl.zxrpc.rpccommon.annotation.PathParam;
import cn.zl.zxrpc.rpccommon.annotation.RequestMapping;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.utils.Preconditions;
import cn.zl.zxrpc.rpccommon.utils.StringUtils;

import java.lang.reflect.Method;

import java.util.*;

public class MethodDescriptor {

    public static enum ArgType {
        Param,
        PathParam,
    }

    public static class ArgDescribe {
        private ArgType argType;
        private int index;
        private String argName;

        public ArgDescribe(ArgType argType, int index) {
            this.argType = argType;
            this.index = index;
        }

        public ArgDescribe(ArgType argType, String argName) {
            this.argType = argType;
            this.argName = argName;
        }

        public static ArgDescribe pathParam(int index) {
            return new ArgDescribe(ArgType.PathParam, index);
        }

        public static ArgDescribe Param(String argName) {
            return new ArgDescribe(ArgType.Param, argName);
        }

        public ArgType getArgType() {
            return argType;
        }

        public int getIndex() {
            return index;
        }

        public String getArgName() {
            return argName;
        }
    }

    private String fullMethodName;
    private String[] args;
    private ArgDescribe[] argDescribes;


    private Class<?>[] argsType;
    private Method method;
    private Object object;
    private Class<?> returnType;
    private Integer argsNumber;


    public MethodDescriptor(String fullMethodName, String[] args, Object o,
                            Method method, Class<?>[] argsType, ArgDescribe[] argDescribes) {
        this.method = method;
        this.returnType = method.getReturnType();
        this.object = Preconditions.checkNotNull(o);
        this.fullMethodName = Preconditions.checkNotNull(fullMethodName, "full method name is not null");
        this.args = args;
        this.argsType = argsType;
        this.argsNumber = args.length;
        this.argDescribes = argDescribes;

    }

    public static Map<String, Integer> getPathParamIfNecessary(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return null;
        }
        Map<String, Integer> pathParam = new HashMap<>();
        String urlPath = requestMapping.value();
        String[] splitUrl = HttpUtils.splitUrl(urlPath);
        int count = 0;
        for (String url : splitUrl) {
            if (url.startsWith(":")) {
                pathParam.put(url.substring(1), count++);
            }
        }
        return pathParam;
    }


    public void handPathVar() {
        String urlPath = method.getAnnotation(RequestMapping.class).value();
        List<Class<?>> pathTypes = new ArrayList<>();
        List<Integer> pathIndex = new ArrayList<>();

        Arrays.stream(method.getParameters()).forEach(parameter -> {
            PathParam pathParam = parameter.getAnnotation(PathParam.class);
            if (pathParam != null) {

                this.argsNumber++;
            }

        });
    }

    public static MethodDescriptor buildMD(Object object, Method method) {

        String methodName = method.getName();
        List<String> args = new ArrayList<>();
        List<Class<?>> argsType = new ArrayList<>();

        Map<String, Integer> pathParamMap = getPathParamIfNecessary(method);
        List<ArgDescribe> argDescribes = new ArrayList<>();
        Arrays.stream(method.getParameters()).forEach(parameter -> {
            //judge parameter have @Param
            Param param = parameter.getAnnotation(Param.class);

            if (param != null) {
                String argName = (StringUtils.isEmpty(param.value()) ? parameter.getName() : param.value());
                args.add(argName);
                argDescribes.add(ArgDescribe.Param(argName));
            }
            PathParam pathParam = parameter.getAnnotation(PathParam.class);
            if (pathParam != null) {
                String argName = pathParam.value();
                Integer argIndex = pathParamMap.get(argName);
                if (argIndex == null) {
                    throw new IllegalArgumentException(argName);
                }
                argDescribes.add(ArgDescribe.pathParam(argIndex));
            }
            //get args type
            Class<?> type = parameter.getType();
            argsType.add(type);

        });
        return new MethodDescriptor(methodName, args.toArray(new String[]{}),
                object, method, argsType.toArray(new Class[]{}), argDescribes.toArray(new ArgDescribe[]{}));
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

    public Class<?>[] getArgsType() {
        return argsType;
    }

    public ArgDescribe[] getArgDescribes() {
        return argDescribes;
    }

    public Integer getArgsNumber() {
        return argsNumber;
    }

}
