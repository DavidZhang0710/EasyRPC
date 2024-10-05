package com.david.easyrpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.david.easyrpc.core.constants.RpcConstants.DEFAULT_DECODE_CHAR;

public class RpcEncoder extends MessageToByteEncoder<com.david.easyrpc.core.common.RpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, com.david.easyrpc.core.common.RpcProtocol msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
        out.writeBytes(DEFAULT_DECODE_CHAR.getBytes());
    }
}
