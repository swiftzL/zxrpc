package cn.zl.rpcserver;

import cn.zl.rpcserver.enventtest.Cat;
import cn.zl.rpcserver.enventtest.User;

import com.alibaba.fastjson.JSON;

/**
 * @Author: zl
 * @Date: 2021/5/6 10:42 下午
 */
public class TestJson {
    public static void main(String[] args) {
        String text = JSON.toJSONString(new User("hahha",3,new Cat("faf"))); //序列化
        System.out.println(text);
    }
}
