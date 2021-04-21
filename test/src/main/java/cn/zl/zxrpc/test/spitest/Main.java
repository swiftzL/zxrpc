package cn.zl.zxrpc.test.spitest;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {

        ServiceLoader<PrintService> load = ServiceLoader.load(PrintService.class);
        load.forEach(e->{
            e.printInfo();
        });
    }
}
