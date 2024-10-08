package com.david.easyrpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.david.easyrpc.core.constants.RpcConstants.MAGIC_NUMBER;

public class RpcDecoder extends ByteToMessageDecoder {

    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            if (!(byteBuf.readShort() == MAGIC_NUMBER)) {
                ctx.close();
                return;
            }
            int length = byteBuf.readInt();
            if (byteBuf.readableBytes() < length) {
                ctx.close();
                return;
            }
            byte[] body = new byte[length];
            byteBuf.readBytes(body);
            com.david.easyrpc.core.common.RpcProtocol rpcProtocol = new com.david.easyrpc.core.common.RpcProtocol(body);
            out.add(rpcProtocol);
        }
    }
}
