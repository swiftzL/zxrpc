package cn.zl.rpcserver.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceMethod implements Invoke {

    private ServerMethodDefinition serverMethodDefinition;

    public ServiceMethod(){

    }

    public ServiceMethod(Object o, Method method) {
        this.serverMethodDefinition = new ServerMethodDefinition(o,method);
    }

    @Override
    public Object invoke(Object... objects) {
        Object o = this.serverMethodDefinition.getMethodDescriptor().getObject();
        Object result = null;
        try {
            result = serverMethodDefinition.getMethodDescriptor().getMethod().invoke(o, objects);
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
