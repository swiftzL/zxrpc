package cn.zl.zxrpc.rpccommon.internal;

import java.util.concurrent.atomic.AtomicLong;

public class LogId {
    private static AtomicLong atomicLong = new AtomicLong(0);

    private String detail;

    private String className;

    private Long id;


    public static LogId getInstance(Class<?> clazz,String detail){
        return new LogId(clazz.getName(),detail);
    }

    private LogId(String className,String detail){
        this.id = atomicLong.getAndIncrement();
        this.className = className;
        this.detail = detail;
    }

    public static Long getNextid(){
        return atomicLong.getAndIncrement();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("the log id isL").append(this.id).append(" this detail is:")
                .append(this.detail).append(" this classname is:").append(this.className);
       return stringBuilder.toString();
    }
}
