package cn.zl.zxrpc.rpccommon.tmpspi;

import cn.zl.zxrpc.rpccommon.execption.FileNotFoundException;
import com.apple.eawt.AppEvent;

/**
 * @Author: zl
 * @Date: 2021/5/4 8:13 下午
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(String name)  {

        System.out.println("the getUser method executed");
        if(name.equals("")){
            System.out.println("exception");
            throw new FileNotFoundException("exception");
        }
        return new User(name);
    }
}
