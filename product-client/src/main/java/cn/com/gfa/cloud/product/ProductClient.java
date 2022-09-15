/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.com.gfa.cloud.product;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERGeneralizedTime;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

public final class ProductClient {

    private EventLoopGroup group;
    private Bootstrap b;
    private String host;
    private int port;

    private final AttributeKey<Promise<ResponseData>> result = AttributeKey.valueOf("result");

    public ProductClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group)
         .channel(NioSocketChannel.class)
         .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(
                        new RequestDataEncoder(),
                        new ResponseDataDecoder(),
                        new ProductClientHandler());
            }
         });
    }

    public void close() {
        // The connection is closed automatically on close.
        group.shutdownGracefully();
    }

    public int send(int operand) {
        Channel ch = null;
        try {
            Promise<ResponseData> promise = group.next().newPromise();

            // Starts the connection 
            ch = b.connect(this.host, this.port).sync().channel();
            Attribute<Promise<ResponseData>> attr = ch.attr(result);
            attr.set(promise);

            // Sends the operand to the server.
            RequestData req = new RequestData(new ASN1Integer(0), new ASN1Integer(operand),
                    new DERGeneralizedTime(new Date()));
            ch.writeAndFlush(req);

            // Waits to receive the response
            ResponseData response = promise.get();
            int result = response.getresult().getValue().intValue();
            ReferenceCountUtil.release(response);

            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (ch != null) {
                ch.close();
            }
        }

        return 0;
    }

}
