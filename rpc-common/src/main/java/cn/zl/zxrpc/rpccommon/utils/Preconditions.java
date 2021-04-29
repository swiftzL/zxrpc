package cn.zl.zxrpc.rpccommon.utils;

public class Preconditions {
    public static <T extends Object> T checkNotNull(T t,Object errmessage){
        if(t==null)
            throw  new NullPointerException(String.valueOf(errmessage));
        return t;
    }

    public static <T extends Object> T checkNotNull(T t){
        return checkNotNull(t,"obj is not null");
    }

    public static <T extends Object> T checkNotNullOrDefault(T t,T defaultValue){
        return StringUtils.isEmpty(String.valueOf(t))?defaultValue:t;
    }


}
