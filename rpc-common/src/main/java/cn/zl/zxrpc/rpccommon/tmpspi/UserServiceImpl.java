package cn.zl.zxrpc.rpccommon.tmpspi;

import cn.zl.zxrpc.rpccommon.annotation.Controller;
import cn.zl.zxrpc.rpccommon.annotation.Param;
import cn.zl.zxrpc.rpccommon.annotation.PathParam;
import cn.zl.zxrpc.rpccommon.annotation.RequestMapping;
import cn.zl.zxrpc.rpccommon.execption.FileNotFoundException;
import cn.zl.zxrpc.rpccommon.message.HttpMethodType;
import com.apple.eawt.AppEvent;

/**
 * @Author: zl
 * @Date: 2021/5/4 8:13 下午
 */
@Controller("/user")
public class UserServiceImpl implements UserService {


    @RequestMapping(value = "/get", httpMethodType = HttpMethodType.GET)
    @Override
    public User getUser(@Param("name") String name) {

        System.out.println("the getUser method executed");
        if (name.equals("")) {
            System.out.println("exception");
            throw new FileNotFoundException("exception");
        }
        return new User(name);
    }

    @RequestMapping(value = "/getuser", httpMethodType = HttpMethodType.GET)
    @Override
    public User getUser(@Param("name") String name, @Param("age") Integer age) {
        if (name.equals("zl")) {
            throw new FileNotFoundException(name);
        }
        User u = new User(name);
        u.setAge(age);
        return u;
    }

    @RequestMapping(value = "/user/:name/:id", httpMethodType = HttpMethodType.GET)
    public User getUserByRestful(@PathParam("name") String name, @PathParam("id") Integer age) {
        if (name.equals("zl")) {
            throw new FileNotFoundException(name);
        }
        User u = new User(name);
        u.setAge(age);
        return u;
    }
    @RequestMapping(value = "/user/:id", httpMethodType = HttpMethodType.GET)
    public User getUserById(@PathParam("id")Integer id,@Param("name")String name){
        User u = new User("default");
        u.setAge(id);
        u.setName(name);
        return u;
    }


}
