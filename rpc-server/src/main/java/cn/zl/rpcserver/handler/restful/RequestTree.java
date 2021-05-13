package cn.zl.rpcserver.handler.restful;

import cn.zl.rpcserver.handler.codec.HttpUtils;
import cn.zl.rpcserver.service.RpcServiceMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/12 2:19 下午
 * 请求调用树
 */
public class RequestTree {

    public static class RequestNodeWrapper {
        RequestNode requestNode;
        int argNumber;

        public RequestNodeWrapper(RequestNode requestNode, int argNumber) {
            this.requestNode = requestNode;
            this.argNumber = argNumber;
        }

        public RequestNode getRequestNode() {
            return requestNode;
        }

        public int getArgNumber() {
            return argNumber;
        }
    }

    private RequestNode root; // '/'

//    private Map<String, RequestNode> routes;  //general urlPath

    public static RequestTree getInstance() {
        RequestTree requestTree = new RequestTree();
        requestTree.setRoot(new RequestNode("/"));
//        this.routes = new HashMap<>();
        return requestTree;
    }

    // user/:id/:name
    public void addRoute(String urlPath, Integer argsNumber, RpcServiceMethod rpcServiceMethod) {
        RequestNode currentNode = this.root;
        String[] urlSplit = HttpUtils.splitUrl(urlPath);//split url
        if (urlPath.equals("")) {
            RequestNode requestNode = new RequestNode();
            requestNode.addMethod(rpcServiceMethod, argsNumber);
            requestNode.setUrlPath(urlPath);
            currentNode.addNode(urlPath, requestNode);
            return;
        }
        int len = urlSplit.length;
        for (int i = 0; i < len; i++) {
            String key = urlSplit[i];
            RequestNode node = currentNode.getNode(key);
            if (node == null) {
                node = new RequestNode(key);
                currentNode.addNode(key, node);
            }
            currentNode = node;
            if (i == len - 1) {
//                RequestNode requestNode = new RequestNode();
                currentNode.addMethod(rpcServiceMethod, argsNumber);
                currentNode.setUrlPath(urlPath);
//                currentNode.addNode(key, requestNode);
            }

        }
    }

    //find method
    public RequestNodeWrapper findNode(String urlPath) {
        RequestNode currentNode = this.root;
        String[] urlSplit = HttpUtils.splitUrl(urlPath);
        int len = urlSplit.length;
        for (int i = 0; i < len; i++) {
            String key = urlSplit[i];
            RequestNode node = currentNode.getNode(key);
            if (node == null) {
                return null;
            } else if (node.getMethod(len - i - 1) != null) {//match arg size
                return new RequestNodeWrapper(node, len - i - 1);
            }
            currentNode = node;
        }
        //end find root
        if (this.root.getMethod(len) != null) {
            return new RequestNodeWrapper(this.root, len);
        }
        return null;
    }

    public RequestNode getRoot() {
        return root;
    }

    public void setRoot(RequestNode root) {
        this.root = root;
    }


}
