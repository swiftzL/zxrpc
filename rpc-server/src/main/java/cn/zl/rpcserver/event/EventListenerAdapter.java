package cn.zl.rpcserver.event;

public abstract class EventListenerAdapter implements EventListener {


    //apply before method
    protected void before(){

    }

    //apply after method
    protected void after(){

    }

    protected abstract void execute();

    public final void doExecute(){
        before();
        execute();
        after();
    }

}
