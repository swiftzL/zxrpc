package cn.zl.zxrpc.rpccommon.internal;

import java.nio.charset.Charset;

public interface Constant {
    String ZXPRC_PROFILE = "zxprc.properties";
    int MAGIC_NUMBER = 0x8d9d3ff3;
    int PONG = 0x039ef;
    int PING = 0x039fe;
    String PROTOCOL_TYPE = "zxrpc";
    Charset UTF_8 = Charset.forName("utf-8");
    String DELIMITER = "\r\n\r\n";
}
