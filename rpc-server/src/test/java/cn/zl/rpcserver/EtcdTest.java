package cn.zl.rpcserver;

import io.etcd.jetcd.Client;

/**
 * @Author: zl
 * @Date: 2021/5/8 4:36 下午
 */
public class EtcdTest {
    public static void main(String[] args) {
        Client client = Client.builder().endpoints("http://localhost:2379").build();

        client.close();


    }
}
