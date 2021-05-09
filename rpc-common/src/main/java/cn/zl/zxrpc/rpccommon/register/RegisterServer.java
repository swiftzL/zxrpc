package cn.zl.zxrpc.rpccommon.register;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zl
 * @Date: 2021/5/9 2:52 下午
 */
public class RegisterServer {
    private String protocol;
    private String host;
    private int port;


    public RegisterServer(){

    }

    public static List<RegisterServer> newList(String protocol,String host){
        RegisterServer registerServer = new RegisterServer();
        registerServer.setProtocol(protocol);
        registerServer.setHost(host);
        return Arrays.asList(registerServer);
    }

    public RegisterServer(String host) {
        this.host = host;
    }

    public RegisterServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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

    public String getUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(protocol).append(host).append(":").append(port);
        return stringBuilder.toString();
    }


}
