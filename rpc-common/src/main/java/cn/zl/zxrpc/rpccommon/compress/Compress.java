package cn.zl.zxrpc.rpccommon.compress;

import cn.zl.zxrpc.rpccommon.extension.SPI;

import java.util.zip.GZIPInputStream;

@SPI("gzip")
public interface Compress {
    byte[] decode(byte[] src);
    byte[] encode();
}
