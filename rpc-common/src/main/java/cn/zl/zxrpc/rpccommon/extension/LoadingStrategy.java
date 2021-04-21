package cn.zl.zxrpc.rpccommon.extension;


/**
 * LoadingStrategy by different directory
 */
public interface LoadingStrategy extends Comparable {
    String getDir();

    @Override
    default int compareTo(Object o){
        return this.hashCode()-o.hashCode();
    }
}
