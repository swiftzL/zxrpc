package cn.zl.zxrpc.rpccommon.utils;


import cn.zl.zxrpc.rpccommon.execption.FileNotFoundException;
import cn.zl.zxrpc.rpccommon.internal.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtils {

    public static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
    private static volatile Properties DEFAULT_PROPERTIES;


    public static Properties getProperties(){
        if(DEFAULT_PROPERTIES==null){
            synchronized (ConfigUtils.class){
                if(DEFAULT_PROPERTIES==null){
                    DEFAULT_PROPERTIES=loadProperties(Constant.ZXPRC_PROFILE);
                }
            }
        }
        return DEFAULT_PROPERTIES;
    }

    public static Properties loadProperties(String filename){
        Properties properties = new Properties();
        File file = new File(filename);
        if(!file.exists())
            throw new FileNotFoundException(filename);
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            logger.debug(e.toString());
        }
        return properties;
    }

}
