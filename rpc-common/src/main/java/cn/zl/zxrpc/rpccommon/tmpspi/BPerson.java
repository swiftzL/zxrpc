package cn.zl.zxrpc.rpccommon.tmpspi;


import cn.zl.zxrpc.rpccommon.extension.Active;

@Active
public class BPerson implements Person {
    @Override
    public void eat() {
        System.out.println("b");
    }
}
