package cn.zl.rpcserver.enventtest;


import cn.zl.rpcserver.event.Event;
import cn.zl.rpcserver.event.EventBroadCast;
import cn.zl.rpcserver.event.internalevent.ShutdownEvent;
import cn.zl.zxrpc.rpccommon.utils.ObjectFactory;
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
//        System.out.println("fds.fdsf.fdsa".replaceAll("\\.","/"));
        instance.fireEvent(ShutdownEvent.class);
        System.out.println(instance.getEventNameToListener());
        instance.fireEvent(()-> System.out.println("this is a event"));

        ObjectFactory<Integer> objectFactory = ObjectFactory.getInstance(()->{
            return new Integer(12);
        });

        System.out.println("-----");
        System.out.println(objectFactory.getObject());


    }




}
