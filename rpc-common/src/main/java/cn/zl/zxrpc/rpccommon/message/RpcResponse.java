package cn.zl.zxrpc.rpccommon.message;

import java.io.Serializable;

public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = -6414324297383809224L;
    private String requestId;

    private Integer code;

    private String message;

    private T data;

    public RpcResponse() {

    }

    public RpcResponse(String requestId, Integer code, String message, T data) {
        this.requestId = requestId;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RpcResponse success(String requestId, Object data) {
        return new RpcResponse(requestId, 200, "ok", data);
    }

    public static RpcResponse fail(String message) {
        //todo rpcContext get
        return new RpcResponse("id", 500, message, null);
    }


    public String getRequestId() {
        return requestId;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
