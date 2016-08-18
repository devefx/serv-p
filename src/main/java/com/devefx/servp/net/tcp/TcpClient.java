package com.devefx.servp.net.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.devefx.servp.net.Client;
import com.devefx.servp.net.HandlerAdapter;

public class TcpClient implements Client {

	protected ChannelFuture future;
	
	@Override
	public void connect(String hostname, int port, HandlerAdapter adapter) {
		EventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new MyChannelInitializer(adapter));
		
		this.future = b.connect(hostname, port);
	}
	
	class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
		
		HandlerAdapter adapter;
		
		MyChannelInitializer(HandlerAdapter adapter) {
			this.adapter = adapter;
		}
		
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new TcpHandlerAdapter(adapter));
		}
	}
	
}
