package cn.zl.rpcserver.enventtest;

import cn.zl.rpcserver.server.Server;
import cn.zl.rpcserver.service.RpcServiceMethod;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import cn.zl.zxrpc.rpccommon.serializer.SerializerType;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import javax.xml.ws.Response;
import java.lang.reflect.Method;

public class ServiceMethod {
    public static void main(String[] args) throws NoSuchMethodException {

        Service service = new Service();
        Class clazz = service.getClass();
        Method method = clazz.getDeclaredMethod("get",String.class);
        Serializer<RpcResponse> rpcSerializer =  Serializer.getInstance(SerializerType.KRYO, RpcResponse.class).register(User.class,Cat.class,RpcResponse.class).build();
        RpcServiceMethod<String> responseRpcServiceMethod = new RpcServiceMethod(service,method,rpcSerializer);
        RpcResponse<String> ff = responseRpcServiceMethod.invoke("ff");
        System.out.println(ff.getData());


    }
}
