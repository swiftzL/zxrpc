package cn.zl.zxrpc.rpccommon.extension;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.sun.tools.javac.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

/**
 * load service find interface realize
 *
 * @param <T> class
 */
public class ServiceLoader<T> {
    public static Logger logger = LoggerFactory.getLogger(ServiceLoader.class);

    private static final ConcurrentHashMap<Class<?>, ServiceLoader<?>> serviceLoadrs = new ConcurrentHashMap<>(64);
    private static final ConcurrentHashMap<Class<?>, Object> SERVICE_INSTANCES = new ConcurrentHashMap<>();
    private static LoadingStrategy[] strategies = loadLoadingStrategies();
    private volatile Map<String, Class<?>> cachaeClasses = null;
    private Class<?> type;
    private String defaultName;
    private volatile ConcurrentHashMap<String, Object> cacheInstance = new ConcurrentHashMap<>(2);

    //load strategies
    private static LoadingStrategy[] loadLoadingStrategies() {
        return stream(load(LoadingStrategy.class).spliterator(), false)
                .sorted()
                .toArray(LoadingStrategy[]::new);
    }


    //add 
    public ServiceLoader(Class<T> type) {
        this.type = type;
    }


    public static <T> ServiceLoader<T> getServiceLoader(Class<T> type) {
        //judge border
        if (type == null) {
            throw new RuntimeException("the type is null");
        }

        ServiceLoader serviceLoader = serviceLoadrs.get(type);
        if (serviceLoader == null) {
            serviceLoader = new ServiceLoader(type);
            serviceLoadrs.putIfAbsent(type, serviceLoader);
        }
        return (ServiceLoader<T>) serviceLoadrs.get(type);
    }

    private synchronized void getServiceClasses() {
        if (this.cachaeClasses == null) {

            this.cachaeClasses = new HashMap();
            loadServiceClass();

        }
    }

    private void loadServiceClass() {

        for (LoadingStrategy strategy : strategies) {
            loadDir(strategy.getDir());
        }
    }

    //load by directory
    private void loadDir(String dir) {
        String filename = dir + type.getName();
        URL resource = type.getClassLoader().getResource(filename);
        loadResource(resource, type.getClassLoader());

    }

    //load by resource
    private void loadResource(URL resource, ClassLoader classLoader) {

        BufferedReader reader = null;
        try {
            //read file
            reader = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
                String line = null;
                while ((line = reader.readLine()) != null && !(line = line.trim()).contains("#") && line.length() > 0) {
                    String[] split = line.split("=");
                    if (split.length == 2) {
                        String key = split[0];
                        String valueStr = split[1];
                        Class<?> valueclass = Class.forName(valueStr.trim(), true, classLoader);
                        loadClass(key.trim(), valueclass);
                    }
                }

        }catch (Exception e) {
            logger.debug(e.toString());
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //load by class
    private void loadClass(String key, Class<?> clazz) {
        if (clazz.isAnnotationPresent(Active.class) || type.getAnnotation(SPI.class).value().trim().equals(key)) {
            this.defaultName = key;
        }
        this.cachaeClasses.put(key, clazz);
    }

    //get default
    public T getDefaultService() {
        getServiceClasses();//if cache is null
        if (!(defaultName.trim() == "")) {
            return getService(defaultName);
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    public T createInstance(Class<?> clazz) {
        if (clazz == null)
            throw new RuntimeException("the class == null");
        try {
            Object o = clazz.newInstance();
            SERVICE_INSTANCES.putIfAbsent(clazz,o);
            return (T)o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("unchecked")
    public T getService(String name) {
        //create obj
        //get class
        Class clazz = cachaeClasses.get(name);
        Object instance = cacheInstance.get(name);
        if (instance == null) {

            synchronized (cacheInstance) {
                if (instance == null) {//double judge
                   instance = createInstance(clazz);
                    cacheInstance.put(name,instance);
                }
            }
        }
        return (T) instance;
    }

}
