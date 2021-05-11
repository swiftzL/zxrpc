package cn.zl.rpcclient.loadbalance;

import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: zl
 * @Date: 2021/5/11 10:53 上午
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public ServiceDescribe select(List<ServiceDescribe> serviceDescribes) {
        int size = serviceDescribes.size();
        return serviceDescribes.get(getIndex() % size);
    }

    private int getIndex() {
        return counter.incrementAndGet();
    }
}
