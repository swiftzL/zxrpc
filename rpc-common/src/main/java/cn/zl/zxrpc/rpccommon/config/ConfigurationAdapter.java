package cn.zl.zxrpc.rpccommon.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ConfigurationAdapter implements Configuration {

    private Map<String,String> metaData = null;


    public ConfigurationAdapter(AbstrctConfig config){
        try {
            metaData = config.getParamters();
        } catch (Exception e){
            throw new RuntimeException("config is error");
        }
    }

    @Override
    public Object getProperty(String key) {
        return metaData.get(key);
    }
}
