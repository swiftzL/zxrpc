package cn.zl.zxrpc.rpccommon.tmpspi;


import cn.zl.zxrpc.rpccommon.extension.Active;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;


public class APerson implements Person{
    @Override
    public void eat() {
        System.out.println("a");
    }

//    public static void main(String[] args) {
//        WritableByteChannel dest = Channels.newChannel (System.out);
//        dest.write(ByteBuffer.allocate(12))
//    }
}
