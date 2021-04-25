package cn.zl.zxrpc.rpccommon.serializer;

public class User {
    private String name;
    private int gae;
    private Cat data;

    public User(String name, int gae,Cat data) {
        this.name = name;
        this.gae = gae;
        this.data = data;
    }



    public User(){

    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", gae=" + gae +
                ", data=" + data +
                '}';
    }
}
