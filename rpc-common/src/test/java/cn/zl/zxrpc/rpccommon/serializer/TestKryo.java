package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.compress.gizp.GzipCompress;
import cn.zl.zxrpc.rpccommon.serializer.kryo.KryoBuilder;

import java.io.ByteArrayInputStream;

public class TestKryo {
    public static void main(String[] args) {

        GzipCompress compress = new GzipCompress();
        for(int i=0;i<2;i++){
            new Thread(()->{
                    RpcSerializer rpcSerializer = Serializer.getInstance(SerializerType.KRYO).register(User.class).build();
            User user = new User("zl",18,new Cat("nb"));
            byte[] bytes = rpcSerializer.encode(user);
            byte[] enbytes = compress.encode(bytes);
            User user1 = rpcSerializer.decode(compress.decode(enbytes), User.class);
            System.out.println(user1);

            User user2 = new User("ljw",12,new Cat("nb"));
            byte[] bytes2 = rpcSerializer.encode(user2);
            System.out.println(rpcSerializer.decode(bytes2,User.class));
            }).start();
        }

//        Kryo kryo = new Kryo();
//        kryo.register(User.class);
//
//        User object = new User("zl",18);
////        object.value = "Hello Kryo!";
//
//        Output output = new Output(new ByteBufferOutput());
//        kryo.writeObject(output, object);
//        byte[] bytes = output.toBytes();
//        System.out.println(bytes.length);
//        System.out.println(object);
//
//
//        Input input = new Input(new ByteBufferInput(bytes));
//        User object2 = kryo.readObject(input, User.class);
//        input.close();
//        System.out.println(object2);






    }
}
