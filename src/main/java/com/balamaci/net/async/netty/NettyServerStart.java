package com.balamaci.net.async.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServerStart {

    public static final int PORT = 8081;

    public static void main(String[] args) {
        // Event loop group to Handle I/O operations for channel
        EventLoopGroup bossGroup = new NioEventLoopGroup();	// (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // (2)

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup) // associate event loop to channel
                .channel(NioServerSocketChannel.class) //
                .childHandler(new ServerConnectionInitializer()) // Add channel initializer
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            // Connect to listening server
            ChannelFuture channelFuture = serverBootstrap.bind("localhost", PORT).sync();
            // Check if channel is connected
            if(channelFuture.isSuccess()) System.out.println("Server started") ;
            // Block till channel is connected
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("closing");
//            eventLoopGroup.shutdownGracefully();
        }
    }

}
