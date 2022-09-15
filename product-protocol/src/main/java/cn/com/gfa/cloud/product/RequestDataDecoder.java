package cn.com.gfa.cloud.product;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class RequestDataDecoder extends ReplayingDecoder<RequestData> {

  @Override
  protected void decode(ChannelHandlerContext ctx,
      ByteBuf in, List<Object> out) throws Exception {

    int len = in.readInt();
    byte data[] = new byte[len];
    in.readBytes(data);

    RequestData req = RequestData.getInstance(data);

    out.add(req);
  }
}
