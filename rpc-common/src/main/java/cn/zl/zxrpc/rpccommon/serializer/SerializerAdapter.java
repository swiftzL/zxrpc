package cn.zl.zxrpc.rpccommon.serializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class SerializerAdapter implements RpcSerializer {

    protected Set<Class<?>> registerClass = new HashSet<>();
    public static Set<Class<?>> basicType = new HashSet<>();

    static{
        basicType.add(Integer.class);

    }

    @Override
    public RpcSerializer build() {
        return null;
    }

    @Override
    public byte[] encode(Object o) {
        return null;
    }

    @Override
    public Object decode(byte[] bytes) {
        return null;
    }

    public RpcSerializer  register(Class clazz){
        registerClass.add(clazz);
        Arrays.stream(clazz.getFields()).filter(this::isBasicType).forEach(this::register);
        return this;
    }
    public RpcSerializer register(Field field){
        registerClass.add(field.getClass());
        return this;
    }
    public  boolean isBasicType(Field t){
        return true;
    }
    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
