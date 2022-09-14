package cn.com.gfa.cloud.product;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {

    @Override
    protected void encode(ChannelHandlerContext ctx, 
      RequestData msg, ByteBuf out) throws Exception {
 
        out.writeInt(msg.getOperand());
    }
}