package cn.zl.zxrpc.rpccommon.utils;

import com.alibaba.fastjson.JSON;

/**
 * @Author: zl
 * @Date: 2021/5/7 7:55 下午
 */
public class TypeConvert {

    public static Object convert(String src, Class<?> clazz) {
        if (src == null) {
            return null;
        }
        if (clazz == String.class) {
            return src;
        } else if (clazz == Integer.class) {
            return new Integer(src);
        } else if (clazz == Long.class) {
            return new Long(src);
        } else {//if src is json
            return JSON.parseObject(src, clazz); //反序列化
        }


    }


}
