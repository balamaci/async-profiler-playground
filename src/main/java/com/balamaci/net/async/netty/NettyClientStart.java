package com.balamaci.net.async.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClientStart {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//        EventLoopGroup eventLoopGroup = new EpollEventLoopGroup(); --use instead for native

        // Help boot strapping a channel
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap
                .group(eventLoopGroup) // associate event loop to channel
//                .channel(EpollSocketChannel.class) -- use instead for native
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ClientChannelHandler());
                    }
                });

        try {
            // Connect to listening server
            ChannelFuture channelFuture = clientBootstrap.connect("localhost", NettyServerStart.PORT).sync();
            // Check if channel is connected
            if(channelFuture.isSuccess()) {
                System.out.println("Connected");
            }
            // Block till channel is connected
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
