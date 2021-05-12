package cn.zl.rpcserver.handler.restful;

import cn.zl.rpcserver.service.RpcServiceMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/12 1:43 下午
 */
public class RequestNode {
    private String urlPath;
    private Map<String, RequestNode> urlToNode;
    private Map<Integer, RpcServiceMethod> argsNumberToMethod;
//    private RpcServiceMethod serviceMethod;
//    private Integer argNumber;

    public RequestNode() {
        this.argsNumberToMethod = new HashMap<>();
        this.urlToNode = new HashMap<>();

    }

    public RequestNode(String urlPath) {
        this.urlPath = urlPath;
        this.urlToNode = new HashMap<>();
        this.argsNumberToMethod = new HashMap<>();
    }

//    public RequestNode(String urlPath) {
//        this.urlPath = urlPath;
//        this.serviceMethod = serviceMethod;
//        this.argNumber = argNumber;
//        this.argsNumberToMethod.put(argNumber, serviceMethod);
//    }

    public void addMethod(RpcServiceMethod serviceMethod, Integer argNumber) {
        this.argsNumberToMethod.put(argNumber, serviceMethod);
    }


    public RpcServiceMethod getMethod(Integer argNumber) {
        return this.argsNumberToMethod.get(argNumber);
    }

    public RequestNode getNode(String key) {
        return urlToNode.get(key);
    }

    public void addNode(String urlPath, RequestNode requestNode) {
        if (requestNode != null) {
            this.urlToNode.put(urlPath, requestNode);
        }
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Map<String, RequestNode> getUrlToNode() {
        return urlToNode;
    }


}
