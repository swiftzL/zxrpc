package cn.zl.zxrpc.rpccommon.config;

import cn.zl.zxrpc.rpccommon.utils.ConfigUtils;

public class PropertiesConfiguration implements Configuration {


    @Override
    public Object getProperty(String key) {
        return ConfigUtils.getProperties().getProperty(key);
    }
}
