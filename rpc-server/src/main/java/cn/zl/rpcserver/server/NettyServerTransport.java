package cn.zl.rpcserver.server;

import cn.zl.rpcserver.handler.NettyServerHandler;
import cn.zl.zxrpc.rpccommon.internal.LogId;
import cn.zl.zxrpc.rpccommon.utils.Preconditions;
import com.sun.org.glassfish.gmbal.Description;
import io.netty.channel.Channel;


@Deprecated
public class NettyServerTransport implements ServerTransport {


    private Channel channel;
    private LogId logId;
    private NettyServerHandler nettyServerHandler;
    private boolean enableHttp;

    public NettyServerTransport(Channel channel){
        this.channel = Preconditions.checkNotNull(channel,"channel is not null");
        this.logId = LogId.getInstance(this.getClass(),this.channel.remoteAddress().toString());
        this.nettyServerHandler = createHandler(this.channel);
    }


    public void start(){

    }

    //simple create
    public NettyServerHandler createHandler(Channel channel){

        return null;
    }

    public Channel getChannel() {
        return channel;
    }

    public LogId getLogId() {
        return logId;
    }



    @Override
    public void shutdownNow() {

    }
}
