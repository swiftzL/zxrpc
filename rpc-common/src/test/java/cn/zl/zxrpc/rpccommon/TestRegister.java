package cn.zl.zxrpc.rpccommon;

import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdRegister;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: zl
 * @Date: 2021/5/9 7:14 下午
 */
public class TestRegister {
    public static void main(String[] args) throws Exception {
        List<RegisterServer> registerServers = RegisterServer.newList("http://", "192.168.31.101");
        EtcdRegister etcdRegister = new EtcdRegister(registerServers);
        etcdRegister.doRegister("cn.zl.service");

    }
}
