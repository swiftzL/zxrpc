package cn.zl.zxrpc.rpccommon.tmpspi;

public class BPerson implements Person {
    @Override
    public void eat() {
        System.out.println("b");
    }
}
