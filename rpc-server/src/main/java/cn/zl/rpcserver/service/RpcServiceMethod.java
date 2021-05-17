package cn.zl.rpcserver.service;

import cn.zl.rpcserver.ratelimiter.Function;
import cn.zl.rpcserver.ratelimiter.RateLimiter;
import cn.zl.rpcserver.ratelimiter.RateLimiterConfig;
import cn.zl.rpcserver.ratelimiter.SupplierFunction;
import cn.zl.zxrpc.rpccommon.annotation.Limiter;
import cn.zl.zxrpc.rpccommon.message.RpcRequest;
import cn.zl.zxrpc.rpccommon.message.RpcResponse;
import cn.zl.zxrpc.rpccommon.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.Duration;

public class RpcServiceMethod<T> extends ServiceMethod {

    private Logger logger = LoggerFactory.getLogger(RpcServiceMethod.class);

    private Serializer<RpcResponse<T>> serializer;

    private Serializer<RpcRequest> rpcRequestSerializer;

    private boolean isLimiter;
    private SupplierFunction supplierFunction;

    private Function<Object, Object> limiterFunction;

    public RpcServiceMethod(Class<?> clazz, Object o, Method m, Serializer<RpcResponse<T>> serializer
            , Serializer<RpcRequest> rpcRequestSerializer) throws NoSuchMethodException {
        //create rpc service method
        super(o, m, clazz);
        this.serializer = serializer;
        this.rpcRequestSerializer = rpcRequestSerializer;
        Limiter limiter = m.getAnnotation(Limiter.class);
        if (limiter == null) {
            limiter = clazz.getAnnotation(Limiter.class);
        }
        if (limiter != null) {
            this.isLimiter = true;
            RateLimiterConfig rateLimiterConfig = new RateLimiterConfig(Duration.ofSeconds(limiter.intervalTime()), limiter.count());
            RateLimiter rateLimiter = RateLimiter.of(this.methodSignature(), rateLimiterConfig);
            this.limiterFunction = RateLimiter.decorateFunction(rateLimiter, (object) -> {
                return super.invoke((Object[]) object);
            });
        }

    }

    public Object invokeToObject(Object... args) throws Exception {
        if (isLimiter && limiterFunction != null) {
            return limiterFunction.run(args);
        }
        return super.invoke(args);
    }


    public RpcResponse<T> invoke(Object... args) throws Exception {
        Object object = super.invoke(args);
        return new RpcResponse("", 12, "", (T) object);
    }

    public byte[] getResponseBytes(Object... args) throws Exception {
        return this.serializer.encode(invoke(args));
    }

    public byte[] getResponseBytes(RpcRequest rpcRequest) throws Exception {
        return this.serializer.encode(invoke(rpcRequest.getArgs()));
    }

    public byte[] getResponseBytes(byte[] rpcRequestBytes) throws Exception {
        RpcRequest rpcRequest = this.rpcRequestSerializer.decode(rpcRequestBytes);
        return getResponseBytes(rpcRequest);
    }

    public Serializer<RpcResponse<T>> getSerializer() {
        return serializer;
    }

    public String methodSignature() {
        return super.methodSignature();
    }

}
