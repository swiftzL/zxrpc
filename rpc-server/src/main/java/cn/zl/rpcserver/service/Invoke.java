package cn.zl.rpcserver.service;

public interface Invoke {
    //invoke method
    Object invoke(Object... objects) throws Throwable;


}
