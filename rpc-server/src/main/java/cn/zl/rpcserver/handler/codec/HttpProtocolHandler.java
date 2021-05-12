package cn.zl.rpcserver.handler.codec;

import cn.zl.rpcserver.handler.restful.RequestNode;
import cn.zl.rpcserver.handler.restful.RequestTree;
import cn.zl.rpcserver.service.MethodDescriptor;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.rpcserver.service.ServiceMethod;
import cn.zl.zxrpc.rpccommon.message.HttpRequest;
import cn.zl.zxrpc.rpccommon.message.JsonResponse;
import cn.zl.zxrpc.rpccommon.tmpspi.Cat;
import cn.zl.zxrpc.rpccommon.utils.TypeConvert;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/7 3:28 下午
 */
public class HttpProtocolHandler {
    //server method url -> service
    private Map<String, RpcServiceMethod> urlMapping;

    private RequestTree requestTree;

    public HttpProtocolHandler() {

    }

    public HttpProtocolHandler(Map<String, RpcServiceMethod> urlMapping) {
        this.urlMapping = urlMapping;
    }

    public void handleHttp(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        HttpRequest httpRequest = HttpUtils.parse(byteBuf);
        String url = httpRequest.getUrl(); //get url
        System.out.println(url);
        if ("/favicon.ico".equals(url.trim())) {//skip icon
            System.out.println("true");
            return;
        }
        //get service method
        RpcServiceMethod serviceMethod = urlMapping.get(url);
        if (serviceMethod == null) {
            //if url is null try get restful api call
            RequestTree.RequestNodeWrapper nodeWrapper = requestTree.findNode(url);
            if (nodeWrapper != null) {
                //get pathParam
                Map<String, String> parameters = httpRequest.getParameters();
                RequestNode requestNode = nodeWrapper.getRequestNode();
                String[] pathValue = url.replaceAll(requestNode.getUrlPath()+"/", "").split("/");
                serviceMethod = requestNode.getMethod(nodeWrapper.getArgNumber());
                Class<?>[] argsType = serviceMethod.getServerMethodDefinition().getMethodDescriptor().getArgsType();
                MethodDescriptor.ArgDescribe[] argDescribes = serviceMethod.getServerMethodDefinition().getMethodDescriptor().getArgDescribes();
                Object[] args = new Object[argsType.length];
                //获取参数
                for (int i = 0; i < argsType.length; i++) {
                    MethodDescriptor.ArgDescribe argDescribe = argDescribes[i];
                    Class<?> clazz = argsType[i];
                    if (argDescribe.getArgType() == MethodDescriptor.ArgType.Param) {
                        args[i] = TypeConvert.convert(parameters.get(argDescribe.getArgName()), clazz);
                    } else if (argDescribe.getArgType() == MethodDescriptor.ArgType.PathParam) {
                        args[i] = TypeConvert.convert(pathValue[argDescribe.getIndex()], clazz);
                    }
                }
                Object result = serviceMethod.invokeToObject(args);
                String text = JSON.toJSONString(JsonResponse.success(result)); //序列化
                ctx.write(HttpUtils.generateHttpResponse(text));

            } else {
                ctx.write(urlNotFount()); //404
                return;
            }

        } else {
            //obtain args
            Map<String, String> parameters = httpRequest.getParameters();
            //get
            String[] argsNames = serviceMethod.getServerMethodDefinition().getMethodDescriptor().getArgs();
            Class<?>[] argsTypes = serviceMethod.getServerMethodDefinition().getMethodDescriptor().getArgsType();
            Object[] args = new Object[argsNames.length];
            for (int i = 0; i < argsNames.length; i++) {
                String argName = argsNames[i];
                String value = parameters.get(argName);
                Class<?> clazz = argsTypes[i];
                args[i] = TypeConvert.convert(value, clazz);//convert type
            }
            Object result = serviceMethod.invokeToObject(args);
            String text = JSON.toJSONString(JsonResponse.success(result)); //序列化
            ctx.write(HttpUtils.generateHttpResponse(text));
        }


    }

    public static ByteBuf urlNotFount() {
        return HttpUtils.generateHttpResponse(JSON.toJSONString(
                new JsonResponse(404, "url is not found", null)));
    }

    public void setRequestTree(RequestTree requestTree) {
        this.requestTree = requestTree;
    }
}
