package com.balamaci.net.async.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ThreadLocalRandom;

import static com.balamaci.net.async.netty.ServerChannelHandler.HELLO_STR;

public class ClientChannelHandler extends SimpleChannelInboundHandler<String>  {

    ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    String[] persons = {"John", "Amanda", "Jack", "Bron", "Bran", "Aria", "Hodor", "Gandalf",
            "Pipin", "Galadriel", "Frodo", "Harry", "Dumbledore", "Ron", "Hermione"};


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String readStr) {
        System.out.println("Received: " + readStr);
        if(readStr.equals(HELLO_STR)) {
            ctx.channel().writeAndFlush(persons[threadLocalRandom.nextInt(10)]);
        }
    }
}
