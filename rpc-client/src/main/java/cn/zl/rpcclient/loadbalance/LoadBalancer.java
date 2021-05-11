package cn.zl.rpcclient.loadbalance;

import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;

import java.util.List;

/**
 * @Author: zl
 * @Date: 2021/5/11 8:58 上午
 */
public interface LoadBalancer {

    public ServiceDescribe select(List<ServiceDescribe> serviceDescribes);


}
