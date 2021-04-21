package cn.zl.zxrpc.rpccommon.extension;

public class ServiceLoadingStrategy implements LoadingStrategy {
    @Override
    public String getDir() {
        return "META-INF/services/";
    }
}
