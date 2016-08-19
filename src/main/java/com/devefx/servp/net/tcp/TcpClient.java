package com.devefx.servp.net.tcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.devefx.servp.net.Client;
import com.devefx.servp.net.Listener;

public class TcpClient implements Client {

	private static final Logger log = LoggerFactory.getLogger(TcpServer.class);
	
	protected List<Listener> listeners = new ArrayList<Listener>();
	
	protected ChannelFuture future;
	
	@Override
	public void connect(String hostname, int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new MyChannelInitializer(listeners));
		
		this.future = b.connect(hostname, port).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					log.info("连接服务器成功");
				} else {
					throw new IOException(future.cause());
				}
			}
		});
	}
	
	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
		
		List<Listener> listeners;
		
		MyChannelInitializer(List<Listener> listeners) {
			this.listeners = listeners;
		}
		
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new TcpHandlerAdapter(listeners));
		}
	}
	
}
