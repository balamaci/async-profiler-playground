package com.balamaci.net.async.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientConnectionInitializer extends ChannelInitializer<SocketChannel>  {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new StringEncoder()); // (1)
        socketChannel.pipeline().addLast(new StringDecoder());// (2)
        socketChannel.pipeline().addLast(new ClientChannelHandler()); // (3)
    }

}
