package cn.zl.rpcserver.server;

import cn.zl.rpcserver.service.RpcServiceMethod;

import java.net.SocketAddress;

public interface Server {

    boolean start();
    boolean isShutdown();
    boolean shutdown();
    int getPort();

    RpcServiceMethod getServiceMethod(String methodSignature);

    //get services
    SocketAddress getListenSocket();


}
