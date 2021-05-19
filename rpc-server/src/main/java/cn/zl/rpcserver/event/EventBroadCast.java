package cn.zl.rpcserver.event;

import cn.zl.rpcserver.netty.DefaultThreadFactory;
import cn.zl.zxrpc.rpccommon.utils.Preconditions;
import cn.zl.zxrpc.rpccommon.utils.ReflectUtil;
import cn.zl.zxrpc.rpccommon.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class EventBroadCast {

    //one event -> more eventListener
    private Map<String, List<EventListener>> eventNameToListener;
    private Map<Class<? extends Event>, String> eventStringMap;
    private ThreadPoolExecutor threadPoolExecutor;
    private static final String DEFAULT_EVENT_THREAD_FACTORY_NAME = "zxrpc-envet";
    private static final int DEFAULT_THREAD_NUM = 10;
    private static Logger logger = LoggerFactory.getLogger(EventBroadCast.class);

    private static Set<Class<?>> eventAnnotations;
    private static Set<Class<?>> eventListenerAnnotations;

    static {
        //initial set
        eventAnnotations = new HashSet<>();
        eventAnnotations.add(EventAn.class);

        eventListenerAnnotations = new HashSet<>();
        eventListenerAnnotations.add(EventListenerAn.class);
        //todo support spring event
    }


    private static class EventBroadCastHolder {
        private static String[] DEFAULT_SCAN_PACKAGES = {"cn.zl.rpcserver.event.internalevent"};
        public static EventBroadCast INSTANCE = new EventBroadCast();

        //loading event event listener
        static {
            try {
                INSTANCE.registerEventPackage(DEFAULT_SCAN_PACKAGES);
                INSTANCE.registerEventListenerPackages(DEFAULT_SCAN_PACKAGES);
//                List<Class> cLasses = ReflectUtil.getClasses(packageNameToDir(DEFAULT_SCAN_PACKAGE),
//                        e -> e.getAnnotation(EventAn.class) != null || e.getAnnotation(EventListenerAn.class) != null);
                //dealing classes

            } catch (Exception e) {
                logger.debug("loading default package event occur error" + e);
            }
        }
    }

    private EventBroadCast() {
        //todo auto load all event and eventlistener
        this.eventNameToListener = new ConcurrentHashMap<>();
        this.eventStringMap = new ConcurrentHashMap<>();
        ThreadFactory threadFactory = new DefaultThreadFactory(DEFAULT_EVENT_THREAD_FACTORY_NAME);

        this.threadPoolExecutor = new ThreadPoolExecutor(DEFAULT_THREAD_NUM, DEFAULT_THREAD_NUM,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                //if thread is full,use call runer execute
                threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    }

    public static EventBroadCast getInstance() {
        return EventBroadCastHolder.INSTANCE;
    }

    public void fireEvent(Class<? extends Event> event) {
        //async execute
        String eventname = this.eventStringMap.get(event);
        if (eventname != null) {
            fireEvent(eventname);
        }
        logger.debug("the event -->" + event + "is not found");
        //ignore the event
        return;
    }

    public void fireEvent(String event) {
        List<EventListener> eventListener = this.eventNameToListener.get(event);
        if (eventListener == null) {
            //ignore the event
            logger.debug("the event -->" + event + "is not found");
            return;
        }
        //todo consider add cache
        eventListener.stream().forEach(e -> this.threadPoolExecutor.execute(EventExecuteRunnerWrap.getRunnerWrap(e)));
    }

    //fire temp event
    public void fireEvent(EventListener eventListener) {
        this.threadPoolExecutor.execute(EventExecuteRunnerWrap.getRunnerWrapNoCache(eventListener));
    }

    private static String packageNameToDir(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    public void registerEvent(String eventName, Class<? extends Event> clazz) {
        this.eventStringMap.put(clazz, eventName);
    }

    public void registerEvent(Class<? extends Event> clazz) {
        EventAn eventAn = clazz.getAnnotation(EventAn.class);
        if (eventAn != null) {
            String eventName = Preconditions.checkNotNull(eventAn.value(), clazz.getSimpleName());
            registerEvent(eventName, clazz);
        }
    }


    public void registerEventPackage(String package0) {
        try {
            List<Class<? extends Event>> classes =  ReflectUtil.getClasses(packageNameToDir(package0),
                    e -> e.getAnnotation(EventAn.class) != null,
                    Event.class);
            this.registerEventClass(classes.toArray(new Class[0]));
        } catch (IOException e) {
            logger.debug("loading default package event occur error" + e);
        }
        //dealing classes
    }


    public void registerEventClass(Class<? extends Event>... clazzs) {
        Arrays.stream(clazzs).forEach(this::registerEvent);
    }

    public void registerEventPackage(String... packages) {
        Arrays.stream(packages).forEach(this::registerEventPackage);
    }

    public void registerEventListener(Class<? extends Event> event, Class<? extends EventListener> eventListener) {
        String eventName = this.eventStringMap.get(event);
        if (StringUtils.isEmpty(eventName)){
            return;
        }
        registerEventListener(eventName,eventListener);
    }

    public void registerEventListener(String eventName, Class<? extends EventListener> eventListener) {
        List<EventListener> eventListeners = this.eventNameToListener.get(eventName);
        if (eventListeners == null) {
            //todo can occur same class object
            eventListeners = new LinkedList<>();
        }
        try {
            eventListeners.add(eventListener.newInstance());
            this.eventNameToListener.put(eventName,eventListeners);
        } catch (Exception e) {
            logger.debug("create eventListener " + eventListener.getName() + " occur exception -->" + e);
        }
    }

    public void registerEventListener(Class<? extends EventListener> eventListener) {
        EventListenerAn annotation = eventListener.getAnnotation(EventListenerAn.class);
        Arrays.stream(annotation.value()).forEach(e -> {
            this.registerEventListener(e,eventListener);
        });
    }
    public void registerEventListener(Class<? extends EventListener>... eventListeners) {
        Arrays.stream(eventListeners).forEach(this::registerEventListener);
    }
    public void registerEventListenerPackages(String... packages){
        Arrays.stream(packages).forEach(this::registerEventListenerPackage);
    }

    public void registerEventListenerPackage(String package0){
        try {
            List<Class<? extends EventListener>> classes =  ReflectUtil.getClasses(packageNameToDir(package0),
                    e -> e.getAnnotation(EventListenerAn.class) != null,
                    EventListener.class);
            this.registerEventListener(classes.toArray(new Class[0]));
        } catch (IOException e) {
            logger.debug("loading default package eventListener occur error" + e);
        }
    }

    public Map<String, List<EventListener>> getEventNameToListener() {
        return eventNameToListener;
    }

    public Map<Class<? extends Event>, String> getEventStringMap() {
        return eventStringMap;
    }
}
