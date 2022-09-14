package cn.com.gfa.cloud.product;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProductClientHandler extends ChannelInboundHandlerAdapter {
 
    /* 
    @Override
    public void channelActive(ChannelHandlerContext ctx) 
      throws Exception {
 
        RequestData msg = new RequestData();
        msg.setOperand(123);

        ChannelFuture future = ctx.writeAndFlush(msg);
    }*/

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) 
      throws Exception {
        System.out.println((ResponseData)msg);
        // ctx.close();
    }
}
