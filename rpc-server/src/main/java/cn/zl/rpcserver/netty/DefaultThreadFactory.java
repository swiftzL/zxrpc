package cn.zl.rpcserver.netty;


import io.netty.util.concurrent.FastThreadLocalThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
    private static Logger logger = LoggerFactory.getLogger(DefaultThreadFactory.class);

    private static AtomicInteger poolId = new AtomicInteger();
    private String prefix;
    private final AtomicInteger nextId;
    private boolean daemon;

    public DefaultThreadFactory(String poolName){
        this(poolName+"-"+poolId.incrementAndGet(),new AtomicInteger(),true);
    }

    public DefaultThreadFactory(String prefix, AtomicInteger nextId, boolean daemon) {
        this.prefix = prefix;
        this.nextId = nextId;
        this.daemon = daemon;
    }

    public void ObtainStatus(){
        logger.debug("current thread nums is -->"+nextId.get());
    }




    @Override
    public Thread newThread(Runnable r) {
        //use netty fastThreadLocalThread
        return new FastThreadLocalThread(r,this.prefix+"-"+nextId.incrementAndGet());
    }
}
