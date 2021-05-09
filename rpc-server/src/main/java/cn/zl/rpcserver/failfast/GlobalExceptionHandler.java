package cn.zl.rpcserver.failfast;

import cn.zl.rpcserver.handler.codec.MessageType;
import cn.zl.zxrpc.rpccommon.execption.FileNotFoundException;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;

/**
 * @Author: zl
 * @Date: 2021/5/6 3:22 下午
 */
@ExceptionAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = FileNotFoundException.class,type = MessageType.ZXRPC)
    public RpcResponse handleFileException(Exception e){
        System.out.println(e.getCause());
        return null;
    }

}
