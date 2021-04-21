package cn.zl.zxrpc.rpccommon.filter;

public interface Filter {
    //before
    boolean before();

    void after();
}
