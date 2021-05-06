package cn.zl.rpcserver.intercept;

import cn.zl.rpcserver.handler.codec.MessageDescribe;
import cn.zl.zxrpc.rpccommon.annotation.Order;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;

/**
 * @Author: zl
 * @Date: 2021/5/6 2:31 下午
 */
@Order
public interface Interceptor extends Comparable<Interceptor> {

    boolean before(RpcRequest rpcRequest);


    void after(RpcResponse rpcResponse);

    @Override
    default int compareTo(Interceptor o) {
        int o1order = 0;
        int o2order = 0;
        Order o1Order = this.getClass().getAnnotation(Order.class);
        if (o1Order != null) {
            o1order = o1Order.value();
        }
        Order o2Order = o.getClass().getAnnotation(Order.class);
        if (o2Order != null) {
            o2order = o2Order.value();
        }
        return o1order - o2order;
    }

}
