package cn.zl.zxrpc.rpccommon.register;

/**
 * @Author: zl
 * @Date: 2021/5/9 7:59 下午
 */
public interface ServiceDiscover {

    public ServiceDescribe getService(String serviceName);

}
