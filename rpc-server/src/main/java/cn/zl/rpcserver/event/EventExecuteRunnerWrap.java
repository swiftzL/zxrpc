package cn.zl.rpcserver.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventExecuteRunnerWrap implements Runnable {

    private EventListener eventListener;

    private static final Map<EventListener, EventExecuteRunnerWrap> runnerCache = new ConcurrentHashMap<>();

    private EventExecuteRunnerWrap(EventListener eventListener, boolean isCache) {
        this.eventListener = eventListener;
        //add cache
        if (isCache) {
            runnerCache.putIfAbsent(eventListener, this);
        }
    }


    public static EventExecuteRunnerWrap getRunnerWrap(EventListener eventListener) {
        EventExecuteRunnerWrap eventExecuteRunnerWrap = runnerCache.get(eventListener);
        return eventExecuteRunnerWrap == null ? new EventExecuteRunnerWrap(eventListener, true) : eventExecuteRunnerWrap;
    }

    public static EventExecuteRunnerWrap getRunnerWrapNoCache(EventListener eventListener) {
        return new EventExecuteRunnerWrap(eventListener, false);
    }

    @Override
    public void run() {
        //execute the event listener
        eventListener.doExecute();
    }
}
