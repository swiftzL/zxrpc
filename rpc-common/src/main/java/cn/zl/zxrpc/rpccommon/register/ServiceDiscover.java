package cn.zl.zxrpc.rpccommon.register;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: zl
 * @Date: 2021/5/9 7:59 下午
 */
public interface ServiceDiscover {

    public List<ServiceDescribe> getService(String serviceName);


}
