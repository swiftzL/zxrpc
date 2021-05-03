package cn.zl.zxrpc.rpccommon.serializer.kryo;

import cn.zl.zxrpc.rpccommon.serializer.RpcSerializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerAdapter;
import com.esotericsoftware.kryo.Kryo;

import java.util.HashMap;
import java.util.Map;

public class KryoBuilder extends SerializerAdapter {

//    Map<Class<?>, Serializer> registers = new HashMap<>();
    //not use
//Pool<Output> outputPool = new Pool<Output>(true, false, 16) {
//    protected Output create () {
//        return new Output(1024, -1);
//    }
//};

    // kryo is not thread safe
    static private  ThreadLocal<Kryo> kryos = null;

    @Override
    public RpcSerializer build() {
        if(kryos ==null)
            kryos = new KryoThreadLocal(this);
        return new KryoCodeC(kryos);
    }

    public Kryo getInstance(){
        Kryo kryo = new Kryo();
        this.registerClass.stream().forEach(e->kryo.register(e,kryo.getDefaultSerializer(e)));
        return kryo;
    }

//    public KryoBuilder register(Class clazz){
//       return register(clazz,kryo.getDefaultSerializer(clazz));
//    }

//    public KryoBuilder register(Class clazz, Serializer serializer){
//        registers.put(clazz,serializer);
//        return self();
//    }
    public static KryoBuilder Builder(){
        return new KryoBuilder();
    }


    public KryoBuilder self(){
        return this;
    }


}
