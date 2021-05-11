package cn.zl.rpcclient.loadbalance;

import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: zl
 * @Date: 2021/5/11 9:06 上午
 */
public class RandomLoadBalancer implements LoadBalancer {


    @Override
    public ServiceDescribe select(List<ServiceDescribe> serviceDescribes) {
        ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
        int i = threadRandom.nextInt(serviceDescribes.size());
        return serviceDescribes.get(i);
    }
}
