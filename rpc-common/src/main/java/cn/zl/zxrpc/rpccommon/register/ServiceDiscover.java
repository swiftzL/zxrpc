package cn.zl.zxrpc.rpccommon.register;

import cn.zl.zxrpc.rpccommon.extension.SPI;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: zl
 * @Date: 2021/5/9 7:59 下午
 */
@SPI
public interface ServiceDiscover {

    public List<ServiceDescribe> getService(String serviceName);


}
