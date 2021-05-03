package cn.zl.rpcserver.service;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;


public class ServerMethodDefinition {
    private MethodDescriptor methodDescriptor;
    //is it exposed
    private volatile Boolean exposed;

    public ServerMethodDefinition(MethodDescriptor methodDescriptor, Boolean exposed) {
        this.methodDescriptor = methodDescriptor;
        this.exposed = exposed;
    }

    public ServerMethodDefinition(Object o, Method method) {
        this.methodDescriptor = MethodDescriptor.buildMD(o,method);
    }

    public MethodDescriptor getMethodDescriptor() {
        return methodDescriptor;
    }
}
