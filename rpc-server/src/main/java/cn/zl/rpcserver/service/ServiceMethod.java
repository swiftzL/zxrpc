package cn.zl.rpcserver.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceMethod implements Invoke {

    private ServerMethodDefinition serverMethodDefinition;

    private Class<?> currentClass;

    public ServiceMethod() {

    }

    public ServerMethodDefinition getServerMethodDefinition() {
        return serverMethodDefinition;
    }

    public ServiceMethod(Object o, Method method, Class<?> clazz) {
        this.currentClass = clazz;
        this.serverMethodDefinition = new ServerMethodDefinition(o, method);
    }

    @Override
    public Object invoke(Object... objects) throws Exception {
        Object o = this.serverMethodDefinition.getMethodDescriptor().getObject();
        Object result;

        try {
            result = serverMethodDefinition.getMethodDescriptor().getMethod().invoke(o, objects);
        } catch (IllegalAccessException e) {
            result = null;
        } catch (InvocationTargetException e) {
            throw (Exception)e.getTargetException();
//            result = null;
        }
        return result;
    }

    public String methodSignature() {
        String className = this.currentClass.getName();
        Method method = this.serverMethodDefinition.getMethodDescriptor().getMethod();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameters = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            parameters.append(clazz.getName() + "/");
        }
        return className + "/" + methodName + "/" + parameters.toString() + "/" +
                this.serverMethodDefinition.getMethodDescriptor().getReturnType().getName();
    }

}
