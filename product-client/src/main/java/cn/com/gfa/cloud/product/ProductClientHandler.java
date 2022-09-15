package cn.com.gfa.cloud.product;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Promise;

public class ProductClientHandler extends ChannelInboundHandlerAdapter {

  private final AttributeKey<Promise<ResponseData>> result = AttributeKey.valueOf("result");
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) 
      throws Exception {
        Attribute<Promise<ResponseData>> attr = ctx.channel().attr(result);
        Promise<ResponseData> promise = attr.get();

        ResponseData resp = (ResponseData)msg;
        System.out.println(resp);
        promise.setSuccess(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      Attribute<Promise<ResponseData>> attr = ctx.channel().attr(result);
      Promise<ResponseData> promise = attr.get();

      promise.setFailure(cause);
    }
}
