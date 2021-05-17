package cn.zl.zxrpc.test;

import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class TestApplication {

    public static void main(String[] args) {
        RpcRequest rpcRequest = new RpcRequest(null, "cn.zl.zxrpc.rpccommon.tmpspi.UserService/getUser/java.lang.String//cn.zl.zxrpc.rpccommon.tmpspi.User", "123", new Object[]{"fdas"});
        Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
        byte[] encode = defaultRequestSerializer.encode(rpcRequest);

//        System.out.println(encode.length);

//
        for (int i = 0; i < encode.length; i++) {
            System.out.println(encode[i]);
        }

        //
        System.out.println("-----");

        RpcRequest decode = defaultRequestSerializer.decode(encode);
        System.out.println(decode);

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            ch.pipeline().addLast(new TestChannelHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8080);

//            ByteBuf byteBuf = Unpooled.copiedBuffer("zxrpc",Charset.defaultCharset());







//            System.out.println(byteBuf.readableBytes());
//            for (int i = 0; i < 20; i++) {
                ByteBuf byteBuf = Unpooled.copiedBuffer("zxrpc",Charset.defaultCharset());
                byteBuf.writeBytes(encode);
//                for(int i=0;i<encode.length;i++){
//                    System.out.println(encode[i]);
//                }
                byteBuf.writeCharSequence("\r\n\r\n", Charset.defaultCharset());
                f.channel().writeAndFlush(byteBuf);
                TimeUnit.SECONDS.sleep(1);
//            }
            TimeUnit.SECONDS.sleep(200);






        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }


    }

}
