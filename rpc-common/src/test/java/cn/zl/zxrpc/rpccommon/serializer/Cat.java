package cn.zl.zxrpc.rpccommon.serializer;

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
