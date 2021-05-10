package cn.zl.zxrpc.rpccommon.message;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -6415942297383809224L;
    private String requestId;
    private Header header;
    //service:method:args
    private String url;
    private String seq;
//    private Class<?>[] args;
    private Object[] args;

    public RpcRequest(){

    }


    public RpcRequest(Header header, String url, String seq, Object[] args) {
        this.header = header;
        this.url = url;
        this.seq = seq;
        this.args = args;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    @Override
    public String toString() {
        return "RpcRequest{" +
                "header=" + header +
                ", url='" + url + '\'' +
                ", seq='" + seq + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
