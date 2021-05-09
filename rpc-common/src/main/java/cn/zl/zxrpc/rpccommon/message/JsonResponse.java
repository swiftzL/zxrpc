package cn.zl.zxrpc.rpccommon.message;

/**
 * @Author: zl
 * @Date: 2021/5/7 1:49 下午
 */
public class JsonResponse<T> {

    private Integer Code;
    private String Message;
    private T data;

    public JsonResponse() {

    }

    public JsonResponse(Integer code, String message, T data) {
        Code = code;
        Message = message;
        this.data = data;
    }

    public static JsonResponse success(Object data) {
        return new JsonResponse(200, "成功", data);
    }

    public static JsonResponse fail(Object data) {
        return new JsonResponse(500, "失败", data);
    }
    public static JsonResponse fail(String message){
        return new JsonResponse(500,message,null);
    }



    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
