package cn.zl.zxrpc.rpccommon.serializer;

public class User {
    private String name;
    private int gae;

    public User(String name, int gae) {
        this.name = name;
        this.gae = gae;
    }


    public User(){

    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", gae=" + gae +
                '}';
    }
}
