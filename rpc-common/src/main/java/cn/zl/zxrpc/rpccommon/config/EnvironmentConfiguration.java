package cn.zl.zxrpc.rpccommon.config;

public class EnvironmentConfiguration implements Configuration {

    @Override
    public Object getProperty(String key) {
        String envvalue = System.getenv(key);
        if(envvalue==null||envvalue.trim().equals("")){
            return null;
        }
        return envvalue;
    }
}
