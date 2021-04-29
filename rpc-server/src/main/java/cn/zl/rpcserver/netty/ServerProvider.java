package cn.zl.rpcserver.netty;


import cn.zl.zxrpc.rpccommon.extension.SPI;

@SPI
public abstract class ServerProvider {


    public static ServerProvider serverProvider(){
        //load by spi
        return null;
    }

    protected abstract boolean isAvailable();


    public abstract ServerBuilder<?> builderForPort(int port);
}
