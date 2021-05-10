package cn.zl.zxrpc.rpccommon.config;

import cn.zl.zxrpc.rpccommon.internal.FramworkExt;

public class Environment implements FramworkExt {

    private Configuration environmentConfiguration;
    private Configuration systemConfiguration;
    private Configuration propertiesConfiguration;

    private Configuration compositeConfiguration;


    public Environment(){
        this.environmentConfiguration = new EnvironmentConfiguration();
        this.systemConfiguration = new SystemConfiguration();
        this.propertiesConfiguration = new PropertiesConfiguration();
        this.compositeConfiguration = new CompositeConfiguration(this.environmentConfiguration,this.systemConfiguration,this.propertiesConfiguration);
    }

    public Configuration getEnvironmentConfiguration() {
        return environmentConfiguration;
    }

    public Configuration getSystemConfiguration() {
        return systemConfiguration;
    }

    public Configuration getPropertiesConfiguration() {
        return propertiesConfiguration;
    }

    public Configuration getCompositeConfiguration() {
        return compositeConfiguration;
    }




}
