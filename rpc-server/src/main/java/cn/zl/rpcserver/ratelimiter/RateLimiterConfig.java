package cn.zl.rpcserver.ratelimiter;

import java.time.Duration;

/**
 * @Author: zl
 * @Date: 2021/5/13 5:16 下午
 */
public class RateLimiterConfig {

    private final Duration limitRefreshPeriod;
    private final int limitForPeriod;

    public RateLimiterConfig(Duration limitRefreshPeriod, int limitForPeriod) {
        this.limitRefreshPeriod = limitRefreshPeriod;
        this.limitForPeriod = limitForPeriod;
    }

    public Duration getLimitRefreshPeriod() {
        return limitRefreshPeriod;
    }

    public int getLimitForPeriod() {
        return limitForPeriod;
    }
}
