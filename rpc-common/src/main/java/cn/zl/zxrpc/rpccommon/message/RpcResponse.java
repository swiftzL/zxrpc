package cn.zl.zxrpc.rpccommon.message;

public class RpcResponse<T> {

    private String requestId;

    private Integer code;

    private String message;

    private T data;

    public RpcResponse(String requestId, Integer code, String message, T data) {
        this.requestId = requestId;
        this.code = code;
        this.message = message;
        this.data = data;
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
}
