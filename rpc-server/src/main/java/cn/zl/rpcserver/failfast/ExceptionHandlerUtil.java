package cn.zl.rpcserver.failfast;

import cn.zl.rpcserver.handler.codec.MessageType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zl
 * @Date: 2021/5/6 3:49 下午
 */
public class ExceptionHandlerUtil {

    private Map<MessageType, Map<Class<? extends Exception>, ExceptionHandlerAdapter>> exceptionHandlers;

    static class ExceptionHandlersHolder {
        public static ExceptionHandlerUtil INSTANCE = new ExceptionHandlerUtil();
    }

    public ExceptionHandlerUtil() {
        this.exceptionHandlers = new ConcurrentHashMap<>();
    }

    public static Map<MessageType, Map<Class<? extends Exception>, ExceptionHandlerAdapter>> getExceptionHandlers(){
        return getInstance().exceptionHandlers;
    }

    public static ExceptionHandlerUtil getInstance() {
        return ExceptionHandlersHolder.INSTANCE;
    }

    public static synchronized void loadExceptionHandlers(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        ExceptionAdvice exceptionAdvice = clazz.getAnnotation(ExceptionAdvice.class);
        Object o = clazz.newInstance();
        if (exceptionAdvice == null) {
            return;
        }
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Map<MessageType, Map<Class<? extends Exception>, ExceptionHandlerAdapter>> exceptionHandlers =
                getInstance().exceptionHandlers;
        for (Method method : declaredMethods) {
            ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
            MessageType type = exceptionHandler.type();
            Class<? extends Exception> needHandleException = exceptionHandler.value();
            if (type != null && needHandleException != null) {

                Map<Class<? extends Exception>, ExceptionHandlerAdapter> classExceptionHandlerAdapterMap =
                        exceptionHandlers.get(type);
                if (classExceptionHandlerAdapterMap == null) {
                    classExceptionHandlerAdapterMap = new ConcurrentHashMap<>();
                }
                classExceptionHandlerAdapterMap.put(needHandleException, new ExceptionHandlerAdapter(o, method));
                exceptionHandlers.put(type, classExceptionHandlerAdapterMap);
            }
        }
    }


}
