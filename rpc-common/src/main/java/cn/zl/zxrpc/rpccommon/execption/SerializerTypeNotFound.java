package cn.zl.zxrpc.rpccommon.execption;

public class SerializerTypeNotFound extends RuntimeException {
    public SerializerTypeNotFound(){
        super("the serializer type is not support");
    }
}
