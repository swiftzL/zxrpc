package cn.zl.zxrpc.rpccommon.message;

public class HttpMethod {
    public static final String GET = "GET";
    public static final String POST = "POST";

    private String httpType;

    public HttpMethod(){

    }
    public HttpMethod(String httpType) {
        this.httpType = httpType;
    }
    public int getTypeLength(){
        return this.httpType.length();
    }
}
