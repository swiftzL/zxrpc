package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.serializer.kryo.KryoBuilder;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;

public class TestKryo {
    public static void main(String[] args) {

        for(int i=0;i<2;i++){
            new Thread(()->{
                    RpcSerializer rpcSerializer = KryoBuilder.Builder().register(User.class).build();
            User user = new User("zl",18);
            byte[] bytes = rpcSerializer.encode(user);
            User user1 = rpcSerializer.decode(bytes, User.class);
            System.out.println(user1);

            User user2 = new User("ljw",12);
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