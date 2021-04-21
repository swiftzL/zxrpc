package cn.zl.zxrpc.rpccommon;

import cn.zl.zxrpc.rpccommon.extension.ServiceLoader;
import cn.zl.zxrpc.rpccommon.tmpspi.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.debug("xxx");
        System.out.println(ServiceLoader.getServiceLoader(Person.class).getDefaultService());
    }
}
