package com.balamaci.net.async.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {

    public static final String HELLO_STR = "Hello, what is your name?";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + " Channel Active");

        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
        poolExecutor.scheduleAtFixedRate(() -> {
            ctx.channel().writeAndFlush(HELLO_STR);
        }, 100, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String readStr) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " " + readStr);
        ctx.channel().writeAndFlush("Hello " + readStr);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + " Channel Inactive");
    }



}
