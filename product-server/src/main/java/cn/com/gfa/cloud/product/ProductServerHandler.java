package cn.com.gfa.cloud.product;

import java.util.Date;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERGeneralizedTime;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProductServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        RequestData requestData = (RequestData) msg;
        System.out.println(requestData);

        int result = requestData.getOperand().getValue().intValue() * 2;

        ResponseData responseData = new ResponseData(new ASN1Integer(0), new ASN1Integer(result), new DERGeneralizedTime(new Date()));
        System.out.println(responseData);

        ctx.writeAndFlush(responseData);
    }
}
