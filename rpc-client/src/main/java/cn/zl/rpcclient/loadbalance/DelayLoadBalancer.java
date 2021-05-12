package cn.zl.rpcclient.loadbalance;

import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: zl
 * @Date: 2021/5/11 10:59 上午
 */
public class DelayLoadBalancer implements LoadBalancer {
    private static Logger logger = LoggerFactory.getLogger(DelayLoadBalancer.class);
    //latency
    private int maxDelay;

    @Override
    public ServiceDescribe select(List<ServiceDescribe> serviceDescribes) {

        for (ServiceDescribe serviceDescribe : serviceDescribes) {
            if (serviceDescribe.getDelay() < maxDelay) {
                return serviceDescribe;
            }
        }
        logger.debug("not found service");
        return null;
    }

}
