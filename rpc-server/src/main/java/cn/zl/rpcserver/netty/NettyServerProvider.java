package cn.zl.rpcserver.netty;

public class NettyServerProvider extends ServerProvider {

    //read profile create netty server


    public NettyServerProvider(){

    }

    //builder for port

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    public ServerBuilder<?> builderForPort(int port) {
        return null;
    }
}
