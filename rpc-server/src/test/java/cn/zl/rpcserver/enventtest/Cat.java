package cn.zl.rpcserver.enventtest;

public class Cat {
    private String name;

    public Cat(){

    }
    public Cat(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                '}';
    }
}
