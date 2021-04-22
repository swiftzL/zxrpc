package cn.zl.zxrpc.rpccommon.config;

public class SystemConfiguration implements Configuration {

    @Override
    public Object getProperty(String key) {
        String value = System.getProperty(key);
        return (value==null||value.trim().equals(""))?null:value;
    }
}
