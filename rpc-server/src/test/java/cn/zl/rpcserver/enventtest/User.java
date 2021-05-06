package cn.zl.rpcserver.enventtest;

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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGae() {
        return gae;
    }

    public void setGae(int gae) {
        this.gae = gae;
    }

    public void setData(Object data) {
        this.data = data;
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
