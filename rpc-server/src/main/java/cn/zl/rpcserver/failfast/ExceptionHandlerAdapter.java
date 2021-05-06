package cn.zl.rpcserver.failfast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: zl
 * @Date: 2021/5/6 3:03 下午
 */
public class ExceptionHandlerAdapter<T>  {

    private Object o;
    private Method method;

    public T handlerException(Exception e) throws InvocationTargetException, IllegalAccessException {
        return (T) method.invoke(o,e);
    }
    public ExceptionHandlerAdapter(){

    }

    public ExceptionHandlerAdapter(Object o, Method method) {
        this.o = o;
        this.method = method;
    }
}
