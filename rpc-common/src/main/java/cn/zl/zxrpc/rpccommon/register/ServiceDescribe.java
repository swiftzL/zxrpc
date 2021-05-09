package cn.zl.zxrpc.rpccommon.register;

/**
 * @Author: zl
 * @Date: 2021/5/9 8:01 下午
 */
public class ServiceDescribe {
    private String serviceName;
    private String host;
    private int port;

    public ServiceDescribe(){

    }

    public ServiceDescribe(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
