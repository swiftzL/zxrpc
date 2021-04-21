package cn.zl.zxrpc.test.spitest;

public class PrintServiceImpl implements PrintService {
    @Override
    public void printInfo() {
        System.out.println("info1");
    }
}
