package cn.zl.zxrpc.rpccommon.serializer.kryo;


import cn.zl.zxrpc.rpccommon.serializer.SerializerAdapter;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.kryo5.util.Pool;

import java.io.FileOutputStream;

public class KryoCodeC extends SerializerAdapter {

    //todo use pool
   private ThreadLocal<Kryo> kryos;


    public KryoCodeC(ThreadLocal<Kryo> kryos){
       this.kryos = kryos;
    }

    @Override
    public <T> T decode(byte[] bytes,Class<T> clazz) {
        Input input = new Input(new ByteBufferInput(bytes));
        T object2 = kryos.get().readObject(input,clazz);
        return object2;
    }

    @Override
    public byte[] encode(Object o) {
        Output output = new Output(new ByteBufferOutput());
        kryos.get().writeObject(output, o);
        return output.toBytes();
    }

    public static void main(String[] args) {



//        Output output = outputPool.obtain();
// Use the Output instance here.
//        outputPool.free(output);

//        Kryo kryo = new Kryo();
//        kryo.register(T.class);

    }



}
