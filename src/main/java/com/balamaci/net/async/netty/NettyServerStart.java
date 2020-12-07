package com.balamaci.net.async.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.incubator.channel.uring.IOUringEventLoopGroup;
import io.netty.incubator.channel.uring.IOUringServerSocketChannel;

public class NettyServerStart {

    public static final int PORT = 8081;

    public static void main(String[] args) throws Exception {
//        -- nio
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();

//        --uncomment for native transport
//        EventLoopGroup bossGroup = new EpollEventLoopGroup();
//        EventLoopGroup workerGroup = new EpollEventLoopGroup();

        // Event loop group to Handle I/O operations for channel
        EventLoopGroup bossGroup = new IOUringEventLoopGroup(1);
        EventLoopGroup workerGroup = new IOUringEventLoopGroup(1);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup) // associate event loop to channel
//                .channel(NioServerSocketChannel.class)  -- nio
//                .channel(EpollServerSocketChannel.class) --uncomment for native transport
                .channel(IOUringServerSocketChannel.class) //--uncomment for io_uring
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ServerChannelHandler());
                    }
                }) // Add channel initializer
                .childOption(ChannelOption.SO_REUSEADDR, true);

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
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
