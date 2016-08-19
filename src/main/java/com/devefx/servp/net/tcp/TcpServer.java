package com.devefx.servp.net.tcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.devefx.servp.net.Listener;
import com.devefx.servp.net.Server;

public class TcpServer implements Server {

	private static final Logger log = LoggerFactory.getLogger(TcpServer.class);
	
	private List<Listener> listeners = new ArrayList<Listener>();
	
	@Override
	public void start(int port) throws Exception {
		start("0.0.0.0", port);
	}
	
	@Override
	public void start(String hostname, int port) throws Exception {
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		ServerBootstrap b = new ServerBootstrap();
		b.group(boosGroup, workerGroup)
		 .channel(NioServerSocketChannel.class)
		 .option(ChannelOption.SO_BACKLOG, 10240)
		 .handler(new LoggingHandler(LogLevel.INFO))
		 .childHandler(new MyChannelInitializer(listeners));
		
		ServerInfo info = new ServerInfo();
		info.bootstrap = b;
		info.serverChanneFuture = b.bind(hostname, port).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					log.info("服务器启动成功");
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
	
	static class ServerInfo {
		ServerBootstrap bootstrap;
		ChannelFuture serverChanneFuture;
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
