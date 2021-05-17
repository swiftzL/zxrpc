package cn.zl.zxrpc.rpccommon.register.etcd;

import io.etcd.jetcd.Client;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zl
 * @Date: 2021/5/9 2:09 下午
 */
public class EtcdClientFactory {

    private static List<String> urls;
    private static Client client;
    private static volatile int index = 0;

    static {
        urls = new ArrayList<>();
    }

    public static Client getInstance() {
        return client;
    }

    public static void addUrls(String... urls) {
        for (String url : urls) {
            addUrl(url);
        }
    }

    public static void addUrl(String url) {
        if (client == null) {
            client = Client.builder().endpoints(url).build();
        }
//        client.getLeaseClient().keepAlive()
        if (!urls.contains(url)) {
            urls.add(url);
        }

    }

    public static Client next() {
        if (++index > urls.size() - 1) {
            index = 0;
        }
        String url = urls.get(index);
        client = Client.builder().endpoints(url).build();
        return client;
    }
}
