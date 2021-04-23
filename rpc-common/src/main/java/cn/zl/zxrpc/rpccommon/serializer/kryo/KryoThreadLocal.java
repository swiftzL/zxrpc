package cn.zl.zxrpc.rpccommon.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;

public class KryoThreadLocal extends ThreadLocal<Kryo> {


    private volatile KryoBuilder kryoBuilder;

    public KryoThreadLocal(KryoBuilder kryoBuilder){
        this.kryoBuilder = kryoBuilder;
    }

    @Override
    protected Kryo initialValue() {
        return kryoBuilder.getInstance();
    }
}
