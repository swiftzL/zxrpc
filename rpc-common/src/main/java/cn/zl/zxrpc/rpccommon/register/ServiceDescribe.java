package cn.zl.zxrpc.rpccommon.register;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @Author: zl
 * @Date: 2021/5/9 8:01 下午
 */
public class ServiceDescribe {
    private String serviceName;
    private String host;
    private int port;
    private volatile int delay; //ping 延迟
    private volatile long lastTime;

    public ServiceDescribe(String serviceName, String url) {
        this.serviceName = serviceName;
        String[] split = url.split(":");
        this.host = split[0];
        this.port = Integer.valueOf(split[1]);
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ServiceDescribe(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SocketAddress getSocketAddress() {
        return new InetSocketAddress(getHost(), getPort());
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "ServiceDescribe{" +
                "serviceName='" + serviceName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
