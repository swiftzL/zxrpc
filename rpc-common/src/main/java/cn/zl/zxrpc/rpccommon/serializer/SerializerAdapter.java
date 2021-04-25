package cn.zl.zxrpc.rpccommon.serializer;

import cn.zl.zxrpc.rpccommon.annotation.CanSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class SerializerAdapter implements RpcSerializer {

    private static Logger logger = LoggerFactory.getLogger(SerializerAdapter.class);
    protected Set<Class<?>> registerClass = new HashSet<>();
    public static Set<Class<?>> entityAnnotations = new HashSet<>();

    static {


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

    public RpcSerializer register(Class clazz) {
        if (!registerClass.contains(clazz)) {
            registerClass.add(clazz);
            logger.debug("add class -->",clazz);
        }

        Arrays.stream(clazz.getDeclaredFields()).filter(this::isBasicType).forEach(this::register);
        return this;
    }

    public RpcSerializer register(Field field) {
        Class clazz = field.getType();
        register(clazz);
        return this;
    }

    public boolean isBasicType(Field t) {
        if(t.getType().getAnnotation(CanSerializer.class)!=null){
            return true;
        }
        //annotation judge
        else if (isJdk(t.getType()) || isBasicType(t.getType())) {
            return false;
        }
        return true;
//        for(Annotation annotation:t.getAnnotations()){
//
//        }
    }

    public boolean isBasicType(Class clazz) {
        return clazz.equals(int.class)
                || clazz.equals(boolean.class)
                || clazz.equals(float.class)
                || clazz.equals(double.class)
                || clazz.equals(long.class)
                || clazz.equals(short.class)
                || clazz.equals(byte.class)
                || clazz.equals(short.class);
    }

    public boolean isJdk(Class clazz) {
        return clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.");
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return null;
    }
}
