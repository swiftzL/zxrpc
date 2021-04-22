package cn.zl.zxrpc.rpccommon.config;

import cn.zl.zxrpc.rpccommon.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstrctConfig {

    public Map<String, String> getParamters() throws InvocationTargetException, IllegalAccessException {
        Map<String, String> paramters = new HashMap<>();
        Class clazz = this.getClass();
        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            Parameter parameter = method.getAnnotation(Parameter.class);
            String key = null;
            String value = null;
            if (methodName.startsWith("get")) {
                String parameterKey = parameter.key();
                key = StringUtils.isEmpty(parameterKey) ? methodName.substring(3).toLowerCase() : parameterKey;
                value = (String) method.invoke(this, null);
                paramters.put(key, value);
            }
        }
        return paramters;

    }

}
