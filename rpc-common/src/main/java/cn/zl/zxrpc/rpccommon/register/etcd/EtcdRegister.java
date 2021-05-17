package cn.zl.zxrpc.rpccommon.register.etcd;

import cn.zl.zxrpc.rpccommon.register.AbstractRegistry;
import cn.zl.zxrpc.rpccommon.register.RegisterServer;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.etcd.jetcd.options.PutOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: zl
 * @Date: 2021/5/9 1:59 下午
 */
public class EtcdRegister extends AbstractRegistry {

    private Logger logger = LoggerFactory.getLogger(EtcdClientFactory.class);

    private int DEFAULT_PORT = 2379;

    private Client etcdClient;

    private Set<String> services;

    private volatile long globalLeaseId;
    private int DEFAULT_REQUEST_TIMEOUT = 10 * 1000;

    private int KEEPALIVE_INTERVAL_TIME = 60*10;

    private ReentrantLock clientLock = new ReentrantLock();//use etcdClient shoud hold the lock

//    private String SERVICE_DEFAULT_PORT = "8081";

    public EtcdRegister() {

    }


    public EtcdRegister(List<RegisterServer> urls) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        this.etcdClient = null;
        this.services = new HashSet<>();
        this.registerServers = urls;
        //todo config read
        this.bindIp = "127.0.0.1";
        urls.forEach(e -> {
            if (e.getPort() == 0) {
                e.setPort(DEFAULT_PORT);
                e.setProtocol("http://");//etcd default protocol
            }
            EtcdClientFactory.addUrl(e.getUrl());
        });
        //etcdClient
        this.etcdClient = EtcdClientFactory.getInstance();

        if (isLocalIp(bindIp)) {
            //get ip by register server
            for (RegisterServer registerServer : registerServers) {
                try {
                    String registerIp = registerServer.getHost();
                    Socket socket = new Socket();
                    SocketAddress address = new InetSocketAddress(registerIp, registerServer.getPort());
                    socket.connect(address);
                    this.bindIp = socket.getLocalAddress().getHostAddress();

                    break;
                } catch (Exception e) {
                    continue;
                }

            }
        }
        //keepalive
        this.globalLeaseId = this.etcdClient.getLeaseClient()
                .grant(70*10)
                .get(10 * 1000, TimeUnit.MILLISECONDS)
                .getID();

        //start keepalive
        new Thread(() -> {
            try {
                keepalive();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        logger.debug("current bind ip is-->" + this.bindIp);

    }

    public void keepalive() throws ExecutionException, InterruptedException {
        while (true) {
            Client client = getEtcdClient();
            CompletableFuture<LeaseKeepAliveResponse> future = client.getLeaseClient().keepAliveOnce(this.globalLeaseId);
            future.whenComplete((r, e) -> {
                if (e != null) {
                    updateEtcdClient();
                }
                logger.debug("the response is -->" + r.toString());
                //ignore response
            });
            releaseClient();
            TimeUnit.SECONDS.sleep(KEEPALIVE_INTERVAL_TIME);//default 100 seconds
        }

    }


    @Override
    public void doRegister(String service) throws Exception {
        if (!services.contains(service)) {
            //the service is registered
            String path = toPath(service);
            //创建临时节点
            Client client = getEtcdClient();
            services.add(service);
            logger.debug("add service -->" + path + " the leaseId is--> " + this.globalLeaseId);

            client.getKVClient().put(ByteSequence.from(path, UTF_8),
                    ByteSequence.from(String.valueOf(globalLeaseId), UTF_8),
                    PutOption.newBuilder().withLeaseId(globalLeaseId).build()).
                    get(DEFAULT_REQUEST_TIMEOUT, TimeUnit.MILLISECONDS);
            releaseClient();
        }

    }

    private Client getEtcdClient() {
        clientLock.lock();
        return this.etcdClient;
    }
    private void releaseClient(){
        this.clientLock.unlock();
    }

    private Client updateEtcdClient() {
        try {
            clientLock.lock();
            Client next = EtcdClientFactory.next();
            this.etcdClient = next;
            return this.etcdClient;
        } finally {
            clientLock.unlock();
        }
    }


    private boolean isLocalIp(String ip) {
        if (ip.equals("127.0.0.1") || ip.length() == 0 || ip.equals("0.0.0.0")) {
            return true;
        }
        return false;

    }
}

