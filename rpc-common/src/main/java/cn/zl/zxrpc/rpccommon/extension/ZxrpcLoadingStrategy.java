package cn.zl.zxrpc.rpccommon.extension;

public class ZxrpcLoadingStrategy implements LoadingStrategy {
    @Override
    public String getDir() {
        return "META-INF/zxrpc/";
    }


}
