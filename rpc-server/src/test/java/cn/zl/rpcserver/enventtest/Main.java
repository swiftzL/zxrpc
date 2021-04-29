package cn.zl.rpcserver.enventtest;


import cn.zl.rpcserver.event.Event;
import cn.zl.rpcserver.event.EventBroadCast;
import cn.zl.rpcserver.event.internalevent.ShutdownEvent;
import cn.zl.zxrpc.rpccommon.utils.ReflectUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
//        URL resource = Main.class.getClassLoader().getResource("cn/zl/rpcserver/enventtest/Main.class");
//        System.out.println(ReflectUtil.getClass(resource));
//        System.out.println(ReflectUtil.getResources("cn/zl/rpcserver/event/internalevent"));
        EventBroadCast instance = EventBroadCast.getInstance();
        System.out.println("fds.fdsf.fdsa".replaceAll("\\.","/"));
        instance.fireEvent(ShutdownEvent.class);
        System.out.println(instance.getEventNameToListener());


    }




}
