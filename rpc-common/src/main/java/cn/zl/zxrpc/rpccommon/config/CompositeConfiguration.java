package cn.zl.zxrpc.rpccommon.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CompositeConfiguration implements Configuration {

    private volatile List<Configuration> configurations = new LinkedList<>();

    public CompositeConfiguration(Configuration ...configuration){
        Arrays.stream(configuration).filter(e->!configurations.contains(e)).forEach(configurations::add);
    }

    public void addConfiguration(Configuration configuration){
        if(!configurations.contains(configuration))
            configurations.add(configuration);
    }

    public Boolean containsKey(String key){
        return configurations.stream().allMatch(e->e.containKey(key));
    }

    @Override
    public Object getProperty(String key) {
       for(Configuration configuration : configurations){
           Object result = configuration.getProperty(key);
           if(result!=null){
               return result;
           }
       }
       return null;
    }
}
