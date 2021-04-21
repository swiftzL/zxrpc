package cn.zl.zxrpc.rpccommon.extension;

public class ZxrpcLoadingStrategy implements LoadingStrategy {
    @Override
    public String getDir() {
        return "META-INF/zxrpc/";
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
