package cn.zl.zxrpc.rpccommon.tmpspi;


import cn.zl.zxrpc.rpccommon.extension.Active;


public class APerson implements Person{
    @Override
    public void eat() {
        System.out.println("a");
    }
}
