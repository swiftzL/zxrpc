package cn.zl.rpcserver.ratelimiter;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.nanoTime;


/**
 * @Author: zl
 * @Date: 2021/5/13 3:50 下午
 */
public class AtomicRateLimiter implements RateLimiter {

    private String name;
    private final AtomicReference<State> state;
    private long startTime;

    public AtomicRateLimiter(String name, RateLimiterConfig rateLimiterConfig) {
        this.name = name;
        State state = new State(0, rateLimiterConfig.getLimitForPeriod());
        state.setRateLimiterConfig(rateLimiterConfig);
        this.state = new AtomicReference<>(state);
        this.startTime = nanoTime();
        this.state.get().setRateLimiterConfig(rateLimiterConfig);
    }

    private long currentNanoTime() {
        return nanoTime() - this.startTime;
    }

    @Override
    public boolean acquirePermission(int permits) {
        State state = this.updateState(permits);
        return state.getActivePermissions() >= 0;
    }

    private State updateState(int permits) {
        AtomicRateLimiter.State prev;
        AtomicRateLimiter.State next;
        do {
            prev = this.state.get();
            next = this.getNextState(prev, permits);
        } while (!this.state.compareAndSet(prev, next));
        return next;
    }

    private State getNextState(State state, int permits) {
        long intervalTime = this.currentNanoTime();
        long cycleInNano = state.getRateLimiterConfig().getLimitRefreshPeriod().toNanos();
        long currentCycle = intervalTime / cycleInNano;
        int nextPermissions = state.getActivePermissions();
        long nextCycle = state.getActiveCycle();
        if (currentCycle != nextCycle) {
            nextPermissions = state.getRateLimiterConfig().getLimitForPeriod();
            nextCycle = currentCycle;
        }
        nextPermissions = nextPermissions - permits;
        State nextState = new State(nextCycle, nextPermissions);
        nextState.setRateLimiterConfig(state.getRateLimiterConfig());
        return nextState;
    }

    private static class State {
        private RateLimiterConfig rateLimiterConfig;

        private final long activeCycle;
        private final int activePermissions;


        public State(long activeCycle, int activePermissions) {
            this.activeCycle = activeCycle;
            this.activePermissions = activePermissions;
        }

        public RateLimiterConfig getRateLimiterConfig() {
            return rateLimiterConfig;
        }

        public void setRateLimiterConfig(RateLimiterConfig rateLimiterConfig) {
            this.rateLimiterConfig = rateLimiterConfig;
        }

        public int getActivePermissions() {
            return activePermissions;
        }

        public long getActiveCycle() {
            return activeCycle;
        }
    }
}
