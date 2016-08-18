package com.devefx.servp.net.tcp;

import com.devefx.servp.net.HandlerAdapter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpHandlerAdapter extends ChannelInboundHandlerAdapter {
	
	private HandlerAdapter adapter;
	
	private TcpSession session;
	
	public TcpHandlerAdapter(HandlerAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		session = new TcpSession(ctx);
		adapter.onSessionCreated(session);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		adapter.onSessionMessage(msg, session);
	}
	
}
