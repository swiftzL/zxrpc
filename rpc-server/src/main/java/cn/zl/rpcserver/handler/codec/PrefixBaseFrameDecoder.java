package cn.zl.rpcserver.handler.codec;

import cn.zl.rpcserver.handler.ProtocolJudgeDecorate;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

//prefix is get post zxprc
public class PrefixBaseFrameDecoder extends ByteToMessageDecoder {

    //    private Set<String> prefixes;
    private Set<ByteBuf> prefixByteBufSets;
    private Map<MessageType, Set<ByteBuf>> protocolToByteBufMap;
    //    private String end = "\r\n";//compatible http protocol
    private ByteBuf delimiter = Unpooled.copiedBuffer("\r\n\r\n", Charset.defaultCharset());
    private boolean discardingTooLongFrame;
    private int maxLength = 655535;

    public PrefixBaseFrameDecoder() {

    }

    public PrefixBaseFrameDecoder(ProtocolJudgeDecorate protocolJudgeDecorate,
                                  boolean discardingTooLongFrame,
                                  int maxLength,
                                  String delimiterStr) {
        this.protocolToByteBufMap = protocolJudgeDecorate.getProtocolToByteBuf();
        this.delimiter = Unpooled.copiedBuffer(delimiterStr, Charset.defaultCharset());
        this.maxLength = maxLength;
        this.discardingTooLongFrame = discardingTooLongFrame;
    }

    public PrefixBaseFrameDecoder(ProtocolJudgeDecorate protocolJudgeDecorate) {
        this(protocolJudgeDecorate, true, 65535, "\r\n\r\n");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    private Object decode(ChannelHandlerContext ctx, ByteBuf in) {

        System.out.println(ByteBufUtil.hexDump(in));

        //get prefix index
        PrefixAndIndex prefixIndex = prefixIndexOf(in);
        if (prefixIndex.index == -1) {
            return null;
        }
        //discover
//        if (prefixIndex.index != 0) {
//            in.skipBytes(prefixIndex.index);
//        }

        //get end index;
        int endIndex = indexOf(in, delimiter);
        if (endIndex == -1) {
            return null;
        }
        endIndex += 4;
        // extract frame
        ByteBuf byteBuf;
        byteBuf = in.readRetainedSlice(endIndex - prefixIndex.index);
        if (byteBuf.readableBytes() >= maxLength && discardingTooLongFrame) {
            return null;
        }
        return new MessageDescribe(prefixIndex.protocolType, byteBuf);
    }

    private PrefixAndIndex prefixIndexOf(ByteBuf in) {
        for (MessageType protocolName : protocolToByteBufMap.keySet()) {
            for (ByteBuf byteBuf : protocolToByteBufMap.get(protocolName)) {
                int index = indexOf(in, byteBuf);
                if (index != -1) {
                    return PrefixAndIndex.get(index, protocolName);
                }
            }
        }
        return PrefixAndIndex.get(-1);
    }


    static class PrefixAndIndex {
        int index;
        MessageType protocolType;

        public static PrefixAndIndex prefixAndIndex = new PrefixAndIndex();

        public PrefixAndIndex() {

        }

        public static PrefixAndIndex get(int index, MessageType protocolType) {
            prefixAndIndex.index = index;
            prefixAndIndex.protocolType = protocolType;
            return prefixAndIndex;
        }

        public static PrefixAndIndex get(int index) {
            return get(index, null);
        }

    }


    //use netty support fix
    private static int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {//遍历
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.readableBytes(); needleIndex++) {
                if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {//如果不相等
                    break;
                } else {
                    haystackIndex++;
                    //找到最后 并且不相等
                    if (haystackIndex == haystack.writerIndex() &&
                            needleIndex != needle.readableBytes() - 1) {
                        return -1;
                    }
                }
            }
            if (needleIndex == needle.readableBytes()) {
                // get
                return i;
            }
        }
        return -1;
    }


}
