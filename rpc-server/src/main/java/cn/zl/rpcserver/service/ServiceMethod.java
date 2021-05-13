package cn.zl.rpcserver.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceMethod implements Invoke {

    private ServerMethodDefinition serverMethodDefinition;

    private Class<?> currentClass;

    private Class<?> interfaceClass;

    public ServiceMethod() {

    }

    public ServerMethodDefinition getServerMethodDefinition() {
        return serverMethodDefinition;
    }

    public ServiceMethod(Object o, Method method, Class<?> clazz)  {
        this.currentClass = clazz;
        this.serverMethodDefinition = new ServerMethodDefinition(o, method);
        this.interfaceClass = clazz;
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            Method declaredMethod = null;
            try {
                declaredMethod = interfaceClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
            }catch (NoSuchMethodException e){

            }
            if (declaredMethod == null) {
                continue;
            } else if (methodSignature(method).equals(methodSignature(declaredMethod))) {
                this.interfaceClass = interfaceClass;
                break;
            }
        }
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
            throw (Exception) e.getTargetException();
//            result = null;
        }
        return result;
    }

    public String methodSignature() {
        String className = this.interfaceClass.getName();
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

    private String methodSignature(Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.getName());
        stringBuilder.append(method.getReturnType().toString());
        for (Class<?> clazz : method.getParameterTypes()) {
            stringBuilder.append(clazz.getName());
        }
        return stringBuilder.toString();
    }

}
