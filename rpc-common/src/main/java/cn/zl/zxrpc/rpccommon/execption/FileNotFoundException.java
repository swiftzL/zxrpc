package cn.zl.zxrpc.rpccommon.execption;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String filename){
        super("the file--"+filename+"-- is not found");
    }
}
