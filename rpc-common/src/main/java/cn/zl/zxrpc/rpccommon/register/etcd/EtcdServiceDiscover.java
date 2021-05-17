package cn.zl.zxrpc.rpccommon.register.etcd;

import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import cn.zl.zxrpc.rpccommon.register.ServiceDescribe;
import cn.zl.zxrpc.rpccommon.register.ServiceDiscover;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.watch.WatchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

    private volatile Map<String, List<ServiceDescribe>> servicesCache;

    private volatile Map<String, List<ServiceDescribe>> updateCache;

    //

    public static EtcdServiceDiscover getInstance(List<RegisterServer> servers) {
        EtcdServiceDiscover etcdServiceDiscover = new EtcdServiceDiscover();
        etcdServiceDiscover.init(servers);
        return etcdServiceDiscover;
    }


    public EtcdServiceDiscover(){
        this.servicesCache = new ConcurrentHashMap<>();
        this.updateCache = new ConcurrentHashMap<>();
    }


    public void init(List<RegisterServer> servers) {
        for (RegisterServer registerServer : servers) {
            if (registerServer.getPort() == 0) {
                registerServer.setPort(DEFAULT_PORT);
                registerServer.setProtocol("http://");//etcd default protocol
            }
            EtcdClientFactory.addUrl(registerServer.getUrl());
        }
//        servers.forEach(e -> {
//            if (e.getPort() == 0) {
//                e.setPort(DEFAULT_PORT);
//                e.setProtocol("http://");//etcd default protocol
//            }
//            EtcdClientFactory.addUrl(e.getUrl());
//        });
        this.client = EtcdClientFactory.getInstance();
        this.servicesCache = new ConcurrentHashMap<>();
        this.updateCache = new ConcurrentHashMap<>();
        ByteSequence path = ByteSequence.from(servicePath, UTF_8);
        this.client.getWatchClient().watch(path, (response) -> {
            for (WatchEvent event : response.getEvents()) {
                WatchEvent.EventType eventType = event.getEventType();
                if (eventType == WatchEvent.EventType.DELETE || eventType == WatchEvent.EventType.PUT) {
                    KeyValue keyValue = event.getKeyValue();
                    System.out.println(keyValue.getKey());
                }
            }
        });
    }

    public void initWatch() {

    }

    private String toPath(String service) {
        return servicePath + service + "/";
    }


    @Override
    public List<ServiceDescribe> getService(String serviceName) {
        List<ServiceDescribe> serviceDescribes = this.servicesCache.get(serviceName);
        List<ServiceDescribe> serviceDiscoversUpdate = this.updateCache.get(serviceName);
        if (serviceDiscoversUpdate !=null && serviceDiscoversUpdate.size() > 0) { //get by updateCache
            this.servicesCache.put(serviceName, serviceDiscoversUpdate);
            this.updateCache.remove(serviceName);
            return serviceDiscoversUpdate;
        }
        if (serviceDescribes != null && serviceDescribes.size() > 0) {
            return serviceDescribes;
        }
        List<ServiceDescribe> services = new LinkedList<>();
        String path = toPath(serviceName);
        try {
            this.client.getKVClient().get(ByteSequence.from(path, UTF_8),
                    GetOption.newBuilder().withPrefix(ByteSequence.from(path, UTF_8)).build())
                    .get(10 * 1000, TimeUnit.MILLISECONDS).getKvs().stream()
                    .parallel().forEach(e -> {
                String url = e.getKey().toString(UTF_8).replace(path, "");
                services.add(new ServiceDescribe(serviceName, url));

            });
            this.servicesCache.put(serviceName, services);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.debug("service obtain fail the exception is -->" + e.toString());
            this.client = EtcdClientFactory.next();
            return null;
        }
        return services;
    }
}
