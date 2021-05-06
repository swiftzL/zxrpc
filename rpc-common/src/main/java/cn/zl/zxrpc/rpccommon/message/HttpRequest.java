package cn.zl.zxrpc.rpccommon.message;

import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/6 4:29 下午
 */
public class HttpRequest {

    private String url;
    private String host;
    private Map<String, String> parameters;
    private HttpMethod method;
    private Map<String, String> headers;


    public HttpRequest(String url, Map<String, String> parameters, HttpMethod method, Map<String, String> headers) {
        this.url = url;
        this.host = headers.get("Host");
        this.parameters = parameters;
        this.method = method;
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "url='" + url + '\'' +
                ", host='" + host + '\'' +
                ", parameters=" + parameters +
                ", method=" + method +
                ", headers=" + headers +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
