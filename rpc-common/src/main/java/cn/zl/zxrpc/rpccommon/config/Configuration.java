package cn.zl.zxrpc.rpccommon.config;

public interface Configuration {

    Object getProperty(String key);

    default Object getProperty(String key,Object defaultValue){
        Object o = getProperty(key);
        return o!=null?o:defaultValue;
    }


    default String getString(String key){
        return getString(key,null);
    }
    default String getString(String key,String defaultValue){
        return convert(String.class,key,defaultValue);
    }
    default Integer getInt(String key){
        return getInt(key,null);
    }
    default Integer getInt(String key,Integer defaultValue){
        return convert(Integer.class,key,defaultValue);
    }

    default Boolean getBool(String key,Boolean defaultValue){
        Boolean bool =  convert(Boolean.class,key,defaultValue);
        if(bool==null){
            throw new NumberFormatException("convert type fail please check profile arguments");
        }
        return bool;
    }
    default Boolean getBool(String key){
        return getBool(key,null);
    }


    default <T> T convert(Class<T> clazz,String key,T defaultValue){
       String strvalue = (String) getProperty(key);
       if(strvalue==null)
           return defaultValue;

       Object o = strvalue;
       if(clazz.isInstance(o))
           return clazz.cast(o);

       if(clazz.equals(Boolean.class)||Boolean.TYPE.equals(clazz)){
           o=Boolean.valueOf(strvalue);
       }else if(clazz.equals(Integer.class)||Integer.TYPE.equals(clazz)){
           o=Integer.valueOf(strvalue);
       }else if(clazz.equals(Long.class)||Long.TYPE.equals(clazz)){
           o=Long.valueOf(strvalue);
       }else if(clazz.equals(Byte.class)||Byte.TYPE.equals(clazz)){
           o = Byte.valueOf(strvalue);
       }else if(Short.class.equals(clazz)||Short.TYPE.equals(clazz)){
           o = Short.valueOf(strvalue);
       }else if(Double.class.equals(clazz)||Double.TYPE.equals(clazz)){
           o = Double.valueOf(strvalue);
       }else if(Float.class.equals(clazz)||Float.TYPE.equals(clazz)){
           o = Float.valueOf(strvalue);
        }
       return clazz.cast(o);
    }


    default boolean containKey(String key){
        return getProperty(key)!=null;
    }
}
