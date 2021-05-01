package cn.zl.rpcserver.service;

import java.util.concurrent.atomic.AtomicBoolean;


public class ServerMethodDefinition<Req, Resp> {
    private MethodDescriptor<Req,Resp> methodDescriptor;
    //is it exposed
    private volatile Boolean exposed;

    public ServerMethodDefinition(MethodDescriptor<Req, Resp> methodDescriptor, Boolean exposed) {
        this.methodDescriptor = methodDescriptor;
        this.exposed = exposed;
    }


}
