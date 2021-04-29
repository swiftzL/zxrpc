package cn.zl.zxrpc.rpccommon.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//reflect util
public class ReflectUtil {

    private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    // obtain package all url
    public static List<URL> getResources(String path) throws IOException {
        return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
    }

    public static <T> List<Class<? extends T>> getClasses(String path, Predicate<Class<?>> predicate,Class<T> tClass) throws IOException {
        List<Class<? extends T>> classList = new LinkedList<>();
        for (URL url : getResources(path)) {
            String urlPath = url.getPath();
            String prefix = urlPath.replaceAll(path, "");
            if (!prefix.endsWith("/")) {
                prefix = prefix + "/";
            }
            getAll(prefix, path).stream().filter(predicate).forEach(e->{
                classList.add((Class<? extends T>)e);
            });
        }
        return classList;
    }

    public static List<Class<?>> getAll(String prefix, String path) {
        List<Class<?>> classes = new LinkedList<>();
        File file = new File(prefix+path);
        if (file.isDirectory()) {
            addClassToDir(prefix, file, classes);
        } else if (file.isFile()) {
            addClassToFile(prefix, file, classes);
        }
        return classes;
    }

    private static void addClassToFile(String prefix, File file, List<Class<?>> list) {
        Class<?> clazz = getClass(getClassName(prefix, file.getPath()));
        list.add(clazz);
    }

    private static void addClassToDir(String prefix, File file, List<Class<?>> list) {
        for (File fileT : file.listFiles()) {
            if(fileT.isFile()){
                addClassToFile(prefix,fileT,list);
            }else if(fileT.isDirectory()){
                addClassToDir(prefix,fileT,list);
            }
        }
    }


    public static Class<?> getClass(String className) {
        if (className.endsWith(".class")) {
            return null;
        }
        try {
            //no init static
            Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return clazz;
        } catch (ClassNotFoundException e) {
            logger.debug("the class--> " + className + " load fail");
        }
        return null;
    }

    public static Class<?> getClass(URL url) {
        return getClass(getClassName(url));
    }

    public static String getClassName(URL url) {
        return getClassName(Thread.currentThread().getContextClassLoader().getResource("").getPath(), url.getPath());
    }

    public static String getClassName(String prefix, String full) {
        return full.substring(prefix.length()).replaceAll("/", ".").replace(".class", "");
    }

}
