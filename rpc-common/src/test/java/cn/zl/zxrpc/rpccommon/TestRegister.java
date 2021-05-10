package cn.zl.zxrpc.rpccommon;

import cn.zl.zxrpc.rpccommon.register.Register;
import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdRegister;
import cn.zl.zxrpc.rpccommon.register.etcd.EtcdServiceDiscover;

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
        Register etcdRegister = new EtcdRegister(registerServers);
        etcdRegister.doRegister("cn.zl.service");

        ServiceDiscover instance = EtcdServiceDiscover.getInstance(registerServers);
        List<ServiceDescribe> services = instance.getService("cn.zl.service");
        services.forEach(e->{
            System.out.println(e);
        });

    }
}
