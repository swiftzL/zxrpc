package cn.zl.rpcserver.handler.codec;

import cn.zl.rpcserver.failfast.ExceptionHandlerAdapter;
import cn.zl.rpcserver.failfast.ExceptionHandlerUtil;
import cn.zl.rpcserver.intercept.Interceptor;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.zxrpc.rpccommon.execption.FileNotFoundException;
import cn.zl.zxrpc.rpccommon.internal.Constant;
import cn.zl.zxrpc.rpccommon.message.HttpRequest;
import cn.zl.zxrpc.rpccommon.message.JsonResponse;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerHelper;
import cn.zl.zxrpc.rpccommon.tmpspi.Cat;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zl
 * @Date: 2021/5/4 11:09 上午
 */
public class MixProtocolHandler extends MessageToMessageDecoder<MessageDescribe> {

    private Map<String, RpcServiceMethod> urlToInvoke;

    private int maxBytes;

    private Serializer<RpcResponse> rpcResponseSerializer;
    private Serializer<RpcRequest> rpcRequestSerializer;

    private HttpProtocolHandler httpProtocolHandler;//handler http code
    private List<Interceptor> interceptors = new LinkedList<>();


    private MessageType messageType;
    private Map<MessageType, Map<Class<? extends Exception>, ExceptionHandlerAdapter>> exceptionHandlers =
            ExceptionHandlerUtil.getExceptionHandlers();

    public MixProtocolHandler() {

    }

    public MixProtocolHandler(Map<String, RpcServiceMethod> urlToInvoke, int maxBytes,
                              Serializer<RpcResponse> rpcResponseSerializer,
                              Serializer<RpcRequest> rpcRequestSerializer,
                              Map<String, RpcServiceMethod> urlToMethodMap) {
        this.urlToInvoke = urlToInvoke;
        this.maxBytes = maxBytes;
        this.rpcRequestSerializer = rpcRequestSerializer;
        this.rpcResponseSerializer = rpcResponseSerializer;
        this.httpProtocolHandler = new HttpProtocolHandler(urlToMethodMap);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, MessageDescribe msg, List<Object> out) throws Exception {
        this.messageType = msg.getMessageType();
        ByteBuf byteBuf = msg.getBody();
        if (msg.getMessageType() == MessageType.ZXRPC) {
            //get url
            byteBuf.skipBytes(5);//zxrpc 5 byte
//            int urlLen = byteBuf.readInt();
//            ByteBuf urlByteBuf = byteBuf.readBytes(urlLen);
//            String url = urlByteBuf.toString(Charset.defaultCharset());
            //analyze url


            //get serializeBytes
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes > this.maxBytes) {
                ctx.fireExceptionCaught(new RuntimeException("this bytes is too long-->" + byteBuf.readableBytes()));
            }
            byte[] serializeBytes = new byte[readableBytes - 4];
            byteBuf.readBytes(serializeBytes, 0, readableBytes - 4);


            //get request
//            Serializer<RpcRequest> defaultRequestSerializer = SerializerHelper.getDefaultRequestSerializer();
            RpcRequest rpcRequest = this.rpcRequestSerializer.decode(serializeBytes);
            System.out.println(rpcRequest);

            //interceptor before
            for (Interceptor interceptor : interceptors) {
                if (!interceptor.before(rpcRequest)) {
                    return;
                }
            }

            //analyze url
//            byte[] serializeBytesResponse;
            RpcResponse rpcResponse = urlToInvoke.get(rpcRequest.getUrl()).invoke(rpcRequest.getArgs());
            System.out.println(rpcResponse);

            //interceptor after
            interceptors.forEach(e -> {
                e.after(rpcResponse);
            });

            ByteBuf resultByteBuf = getByteBuf(messageType, rpcResponse);
            ctx.write(resultByteBuf);
        } else if (msg.getMessageType() == MessageType.HTTP) {

            this.httpProtocolHandler.handleHttp(ctx, byteBuf);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("flush");
        ctx.flush();
        if (messageType == MessageType.HTTP) {
            ctx.close();//not support keepalive
        }

    }

    private ByteBuf getByteBuf(MessageType messageType, Object o) {
        if (messageType == MessageType.HTTP) {
            String s = JSON.toJSONString(o);
            ByteBuf byteBuf = HttpUtils.generateHttpResponse(s);
            return byteBuf;
        } else if (messageType == MessageType.ZXRPC) {
            byte[] encode = this.rpcResponseSerializer.encode(o);
            int length = encode.length;
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(Constant.MAGIC_NUMBER);
            buffer.writeInt(length);
            buffer.writeBytes(encode);
            return buffer;
        }
        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //exception handler
        System.out.println(this.messageType == MessageType.ZXRPC);
        System.out.println(cause.getCause().getClass());
        Map<Class<? extends Exception>, ExceptionHandlerAdapter> classExceptionHandlerAdapterMap
                = this.exceptionHandlers.get(this.messageType);
        Exception exception = (Exception) cause.getCause();
        if (classExceptionHandlerAdapterMap != null) {
            ExceptionHandlerAdapter<RpcResponse> exceptionHandlerAdapter = classExceptionHandlerAdapterMap.get(cause.getCause().getClass());
            if (exceptionHandlerAdapter == null) {
                RpcResponse rpcResponse = exceptionHandlerAdapter.handlerException(exception);
                ctx.write(getByteBuf(messageType, rpcResponse));
            } else {
                //use default handler
                Object o = ExceptionHandlerAdapter.DefaultHandler(messageType, exception);
                ctx.write(getByteBuf(messageType, o));

            }
        } else {
            //use default handler
            Object o = ExceptionHandlerAdapter.DefaultHandler(messageType, exception);
            System.out.println(o);
            ctx.write(getByteBuf(messageType, o));

        }


    }
}
