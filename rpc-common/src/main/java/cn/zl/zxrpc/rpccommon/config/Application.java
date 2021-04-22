package cn.zl.zxrpc.rpccommon.config;

public class Application extends AbstrctConfig {
    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getHostname() {
        return hostname;
    }

    private String hostname;

}
