package cn.zl.zxrpc.rpccommon.register;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: zl
 * @Date: 2021/5/9 1:54 下午
 */
public abstract class AbstractRegistry implements Register {

    private String ROOT_NODE = "zxrpc";
    protected Charset UTF_8 = Charset.forName("utf-8");

    protected List<RegisterServer> registerServers;

    protected String bindIp;
    protected Integer bindPort = 8080;

    protected String toPath(String service) {
        return "/" + ROOT_NODE + "/" + "services/" + service + "/" + getHost();
    }

    protected String getHost() {
        return bindIp + ":" + bindPort.toString();
    }

    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }
}
