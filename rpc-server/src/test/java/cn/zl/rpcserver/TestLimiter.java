package cn.zl.rpcserver;

import cn.zl.rpcserver.ratelimiter.Function;
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
    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloService();
        RateLimiterConfig rateLimiterConfig = new RateLimiterConfig(Duration.ofSeconds(1), 3);
        RateLimiter rateLimiter = RateLimiter.of("default", rateLimiterConfig);
        SupplierFunction<String> supplier = RateLimiter.decorateCheckedSupplier(rateLimiter, 1, helloService::sayHello);
//        while (true){
////            for(int i=0;i<100;i++){
//                new Thread(()->{
//                    try {
//                        System.out.println(supplier.get());
//                    }catch (Exception e){
//                        System.out.println(e);
//                    }
//                }).start();
//            }
////            TimeUnit.SECONDS.sleep(1);
//        }




//        System.out.println(function.apply("hfa"));

        Function<String, String> function = RateLimiter.decorateFunction(rateLimiter, (String s) -> {
            return helloService.sayHello(s);
        });
        for(int i=0;i<10;i++){
            new Thread(()->{
                try {
                    System.out.println(function.run("ff"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }


//

    }

}
