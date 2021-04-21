package cn.zl.zxrpc.rpccommon;

import cn.zl.zxrpc.rpccommon.extension.ServiceLoader;
import cn.zl.zxrpc.rpccommon.tmpspi.Person;

public class Main {
    public static void main(String[] args) {
        System.out.println("start");
        System.out.println(ServiceLoader.getServiceLoader(Person.class).getDefaultService());
    }
}
