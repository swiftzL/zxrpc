package cn.zl.rpcserver.handler.codec;

import cn.zl.zxrpc.rpccommon.message.HttpMethod;
import cn.zl.zxrpc.rpccommon.message.HttpRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/6 4:30 下午
 */
public class HttpUtils {
    private static byte[] GET = new byte[]{0x47, 0x45, 0x54};
    private static byte[] DELIMITER = new byte[]{0x0d, 0x0a};
    private static byte blank = 0x20;

    private static byte QuestionMark = 0x3f;
    private static byte EqualMark = 0x3d;
    private static byte ParameterDelimiter = 0x26;//&
    private static byte[] HeaderDelimiter = new byte[]{0x3a, 0x20};
    private static byte[] HttpVersion = "HTTP/1.1".getBytes();
    private static byte[] HttpStatusCode = "200".getBytes();
    private static byte[] HttpStatus = "OK".getBytes();

    public static HttpRequest parse(ByteBuf byteBuf) {
        //read line
        HttpMethod httpMethod = parseMethod(byteBuf);
        if (httpMethod == null) {
            return null;
        }
        String url = parseUrl(byteBuf);
        if (url == null) {
            return null;
        }
        //parse args
        Map<String, String> args = parseParameters(byteBuf);
        Map<String, String> headers = parseHeaders(byteBuf);


        return new HttpRequest(url, args, httpMethod, headers);
    }

    public static HttpMethod parseMethod(ByteBuf byteBuf) {
        int j = 0;
        byte[] bytes = new byte[4];
        for (int i = byteBuf.readerIndex(); i < 4; i++) {
            byte readByte = byteBuf.readByte();
            if (readByte == 0x20) {
                //get method
//                byteBuf.skipBytes(j);
                return new HttpMethod(new String(bytes, 0, j, Charset.defaultCharset()));
            }
            bytes[j++] = readByte;
        }
        return null;
    }

    public static String parseUrl(ByteBuf byteBuf) {

        int start = 0;
        int questionIndex = 0;
        int index = 0;
        for (int i = byteBuf.readerIndex(); i < byteBuf.readableBytes(); i++) {
            byte currentByte = byteBuf.getByte(i);
            index++;
            if (currentByte == QuestionMark) {//find ? index
                questionIndex = index - 1;
                break;
            }
        }
        if (questionIndex != 0) {

            String result = byteBuf.readCharSequence(questionIndex - start, Charset.defaultCharset()).toString();
            byteBuf.skipBytes(1);//skip ?
            return result;
        }
        return null;
    }

    public static Map<String, String> parseHeaders(ByteBuf byteBuf) {
        Map<String, String> headers = new HashMap<>();

        byte[] bytes = new byte[223];
        int kIndex = 0, vIndex = 0, i = 0;
        while (byteBuf.readableBytes() > 0) {
            byte readByte = byteBuf.readByte();
            bytes[i++] = readByte;
            //if : 2f20
            if (readByte == HeaderDelimiter[0] && byteBuf.getByte(byteBuf.readerIndex()) == HeaderDelimiter[1]) {
                //get header value
                kIndex = i - 1;
                byteBuf.skipBytes(1);
            } else if (readByte == DELIMITER[0] && byteBuf.getByte(byteBuf.readerIndex()) == DELIMITER[1] && byteBuf.readableBytes() >= 2) {
                vIndex = i - 1;
                String key = new String(bytes, 0, kIndex, Charset.defaultCharset());
                String value = new String(bytes, kIndex + 1, vIndex - kIndex - 1);
                headers.put(key, value);
                kIndex = vIndex = i = 0;
                byteBuf.skipBytes(1);
            }
            if (byteBuf.readableBytes() < 3) {
                break;
            }
        }
        return headers;
    }

    public static Map<String, String> parseParameters(ByteBuf byteBuf) {

        Map<String, String> parameters = new HashMap<>();
        byte[] bytes = new byte[223];
        int kIndex = 0, vIndex = 0, i = 0;
        //
        while (byteBuf.readableBytes() > 0) {
            byte readByte = byteBuf.readByte();
            bytes[i++] = readByte;
            if (readByte == EqualMark) {
                //get value
                kIndex = i - 1;
            } else if (readByte == ParameterDelimiter || readByte == blank) {
                vIndex = i - 1;
                //handler parameter
                String key = new String(bytes, 0, kIndex, Charset.defaultCharset());
                String value = new String(bytes, kIndex + 1, vIndex - kIndex - 1);
                parameters.put(key, value);
                //reset variable
                i = kIndex = vIndex = 0;
            }
            if (readByte == DELIMITER[0]) {
                byteBuf.readByte();
                break;
            }
        }
        //skip delimiter /r/n
        return parameters;
    }

    public static ByteBuf generateHttpResponse(String response) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(HttpVersion);//Http
        byteBuf.writeByte(blank);
        byteBuf.writeBytes(HttpStatusCode);//200
        byteBuf.writeByte(blank);
        byteBuf.writeBytes(HttpStatus); //Ok
        byteBuf.writeBytes(DELIMITER);
        byteBuf.writeBytes(DELIMITER);
        byteBuf.writeBytes(response.getBytes());
        return byteBuf;
    }


}
