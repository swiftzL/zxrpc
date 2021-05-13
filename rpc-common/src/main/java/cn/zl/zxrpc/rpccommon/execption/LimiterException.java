package cn.zl.zxrpc.rpccommon.execption;

/**
 * @Author: zl
 * @Date: 2021/5/13 5:48 下午
 */
public class LimiterException extends RuntimeException {

    public LimiterException() {
        super("occur limier exception ");
    }
}
