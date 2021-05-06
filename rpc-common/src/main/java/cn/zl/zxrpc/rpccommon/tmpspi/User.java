package cn.zl.zxrpc.rpccommon.tmpspi;

/**
 * @Author: zl
 * @Date: 2021/5/4 8:11 下午
 */
public class User {
    private String name;

    public User(){}

    public User(String name) {

        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
