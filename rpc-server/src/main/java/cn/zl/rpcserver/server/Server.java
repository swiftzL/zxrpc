package cn.zl.rpcserver.server;

import java.net.SocketAddress;

public interface Server {

    boolean start();
    boolean isShutdown();
    boolean shutdown();
    int getPort();

    //get services
    SocketAddress getListenSocket();


}
