package cn.zl.rpcserver.event.internalevent;

import cn.zl.rpcserver.event.EventListenerAdapter;
import cn.zl.rpcserver.event.EventListenerAn;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

@EventListenerAn({ShutdownEvent.class})
public class ShutdownEventListener extends EventListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ShutdownEventListener.class);

    @Override
    protected void before() {
        System.out.println("before call");
    }

    @Override
    protected void after() {
        System.out.println("after method called");
    }

    @Override
    public void execute() {
        System.out.println("execute");
        logger.debug("the server being shutdown");
    }

}
