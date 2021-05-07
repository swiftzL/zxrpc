package cn.zl.zxrpc.rpccommon.tmpspi;

/**
 * @Author: zl
 * @Date: 2021/5/4 8:11 下午
 */
public interface UserService  {

    public User getUser(String name);

    public User getUser(String name,Integer age);

}
