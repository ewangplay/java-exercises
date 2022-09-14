package cn.com.gfa.cloud.product;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class RequestDataDecoder extends ReplayingDecoder<RequestData> {

    @Override
    protected void decode(ChannelHandlerContext ctx, 
      ByteBuf in, List<Object> out) throws Exception {
 
        RequestData data = new RequestData();
        data.setOperand(in.readInt());

        out.add(data);
    }
}
