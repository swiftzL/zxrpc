package cn.zl.rpcserver.intercept;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zl
 * @Date: 2021/5/6 2:40 下午
 */
public class Interceptors {

    private List<Interceptor> list;

    static class InterceptorsHolder {
        static Interceptors interceptors = new Interceptors();
    }

    public static Interceptors getInstance() {
        return InterceptorsHolder.interceptors;
    }


    private Interceptors() {
        this.list = new LinkedList<>();
    }

    public static void addInterceptor(Interceptor interceptor){
        getInstance().list.add(interceptor);
        getInstance().list.sort(Interceptor::compareTo);
    }

}
