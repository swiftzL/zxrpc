package cn.zl.rpcserver.filter;

import javassist.*;

/**
 * @Author: zl
 * @Date: 2021/5/4 10:08 上午
 */
public class FilterObjectFactory {
    public static Filter getFilter(Class<? extends FilterAdapter> filterClass) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool aDefault = ClassPool.getDefault();
        CtClass ctFilterClass = aDefault.get(filterClass.getClass().getName());
        CtMethod ctDofilterMethod = ctFilterClass.getMethod("doFilter",
                "(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;)V;");
        ctDofilterMethod.insertBefore("counter++");
        Class<?> aClass = ctFilterClass.toClass();
        return (Filter) aClass.newInstance();
    }
}
