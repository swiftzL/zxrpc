package cn.zl.rpcserver.handler.codec;

import cn.zl.rpcserver.service.ServiceMethod;
import cn.zl.zxrpc.rpccommon.message.HttpRequest;
import cn.zl.zxrpc.rpccommon.message.JsonResponse;
import cn.zl.zxrpc.rpccommon.tmpspi.Cat;
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
    private Map<String, ServiceMethod> urlMapping;

    public void handleHttp(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        HttpRequest httpRequest = HttpUtils.parse(byteBuf);
        String url = httpRequest.getUrl();
        System.out.println(url);
        if("/favicon.ico".equals(url.trim())){
            System.out.println("true");
            return;
        }
        //obtain args
        Map<String, String> parameters = httpRequest.getParameters();
        System.out.println(parameters);
        //get url

        String text = JSON.toJSONString(JsonResponse.success(new Cat("hahha"))); //序列化
        System.out.println(text);
        ctx.write(HttpUtils.generateHttpResponse(text));


    }
}
