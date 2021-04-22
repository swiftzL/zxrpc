package cn.zl.zxrpc.rpccommon.config;

import cn.zl.zxrpc.rpccommon.internal.FramworkExt;

public class Environment implements FramworkExt {

    private Configuration environmentConfiguration;
    private Configuration systemConfiguration;
    private Configuration propertiesConfiuration;

    private Configuration compositeConfiguration;


    public Environment(){
        this.environmentConfiguration = new EnvironmentConfiguration();
        this.systemConfiguration = new SystemConfiguration();
        this.propertiesConfiuration = new PropertiesConfiguration();
        this.compositeConfiguration = new CompositeConfiguration(this.environmentConfiguration,this.systemConfiguration,this.propertiesConfiuration);
    }

    public Configuration getEnvironmentConfiguration() {
        return environmentConfiguration;
    }

    public Configuration getSystemConfiguration() {
        return systemConfiguration;
    }

    public Configuration getPropertiesConfiuration() {
        return propertiesConfiuration;
    }

    public Configuration getCompositeConfiguration() {
        return compositeConfiguration;
    }




}
