package cn.zl.zxrpc.rpccommon.message;


import java.io.Serializable;
import java.lang.reflect.Type;

public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -6415942297383809224L;
    private Header header;
    //service:method:args
    private String url;
    private String seq;
//    private Class<?>[] args;
    private Object[] args;

    public RpcRequest(Header header, String url, String seq, Object[] args) {
        this.header = header;
        this.url = url;
        this.seq = seq;
        this.args = args;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
