package cn.zl.rpcserver.failfast;

import cn.zl.rpcserver.handler.codec.MessageType;
import cn.zl.zxrpc.rpccommon.message.JsonResponse;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;

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

    //default exception handler
    public static Object DefaultHandler(MessageType messageType,Exception e){
        switch (messageType){
            case HTTP:
                return JsonResponse.fail(e.toString());
            case ZXRPC:
                return RpcResponse.fail(e.toString());
            default:
                return null;
        }
    }
}
