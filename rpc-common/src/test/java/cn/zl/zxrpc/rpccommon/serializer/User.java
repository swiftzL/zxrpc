package cn.zl.zxrpc.rpccommon.serializer;

import java.net.MalformedURLException;
import java.net.URL;

public class User {
    private String name;
    private int gae;
    private Object data;

    public User(String name, int gae,Cat data) {
        this.name = name;
        this.gae = gae;
        this.data = data;
    }

    public Object getData(){
        return this.data;
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

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http","127.0.0.1",8080,"");
        System.out.println(url);
    }
}
