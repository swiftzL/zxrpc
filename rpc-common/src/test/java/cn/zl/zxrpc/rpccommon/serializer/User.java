package cn.zl.zxrpc.rpccommon.serializer;

public class User<T> {
    private String name;
    private int gae;
    private T data;

    public User(String name, int gae,T data) {
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
