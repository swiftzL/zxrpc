package cn.zl.rpcserver;

import cn.zl.rpcserver.ratelimiter.RateLimiter;
import cn.zl.rpcserver.ratelimiter.RateLimiterConfig;
import cn.zl.rpcserver.ratelimiter.SupplierFunction;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zl
 * @Date: 2021/5/13 11:29 下午
 */
public class TestLimiter {
    public static void main(String[] args) throws InterruptedException {
        HelloService helloService = new HelloService();
        RateLimiterConfig rateLimiterConfig = new RateLimiterConfig(Duration.ofSeconds(1),10);
        RateLimiter rateLimiter = RateLimiter.of("default",rateLimiterConfig);
        SupplierFunction<String> supplier = RateLimiter.decorateCheckedSupplier(rateLimiter, 1, helloService::sayHello);
        while (true){
//            for(int i=0;i<100;i++){
                new Thread(()->{
                    try {
                        System.out.println(supplier.get());
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }).start();
            }
//            TimeUnit.SECONDS.sleep(1);
        }


}
