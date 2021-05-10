package cn.zl.zxrpc.rpccommon.register.etcd;

import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.options.GetOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Charsets.UTF_8;

/**
 * @Author: zl
 * @Date: 2021/5/10 1:32 下午
 */
public class EtcdServiceDiscover implements ServiceDiscover {

    private static Logger logger = LoggerFactory.getLogger(EtcdServiceDiscover.class);

    private Client client;

    private int DEFAULT_PORT = 2379;

    private String servicePath = "/zxrpc/services/";

    public static EtcdServiceDiscover getInstance(List<RegisterServer> servers) {
        EtcdServiceDiscover etcdServiceDiscover = new EtcdServiceDiscover();
        etcdServiceDiscover.init(servers);
        return etcdServiceDiscover;
    }

    public void init(List<RegisterServer> servers) {
        for (RegisterServer registerServer : servers) {
            if (registerServer.getPort() == 0) {
                registerServer.setPort(DEFAULT_PORT);
                registerServer.setHost("http://");//etcd default protocol
            }
            EtcdClientFactory.addUrl(registerServer.getUrl());
        }
        this.client = EtcdClientFactory.getInstance();
    }

    private String toPath(String service) {
        return servicePath + service+"/";
    }


    @Override
    public List<ServiceDescribe> getService(String serviceName) {
        List<ServiceDescribe> services = new LinkedList<>();
        String path = toPath(serviceName);
        try {
            this.client.getKVClient().get(ByteSequence.from(path, UTF_8),
                    GetOption.newBuilder().withPrefix(ByteSequence.from(path, UTF_8)).build())
                    .get(10 * 1000, TimeUnit.MILLISECONDS).getKvs().stream()
                    .parallel().forEach(e -> {
                String url = e.getKey().toString(UTF_8).replace(path,"");
                services.add(new ServiceDescribe(serviceName, url));

            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.debug("service obtain fail the exception is -->" + e.toString());
            return null;
        }
        return services;
    }
}
