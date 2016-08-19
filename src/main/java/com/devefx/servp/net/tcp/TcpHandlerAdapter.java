package com.devefx.servp.net.tcp;

import java.util.Iterator;
import java.util.List;

import com.devefx.servp.net.Listener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpHandlerAdapter extends ChannelInboundHandlerAdapter {
	
	private TcpSession session;
	
	private List<Listener> listeners;
	
	public TcpHandlerAdapter(List<Listener> listeners) {
		this.listeners = listeners;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		session = new TcpSession(ctx);
		Iterator<Listener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onCreated(session);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Iterator<Listener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onMessage(msg, session);
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Iterator<Listener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onClose(session);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Iterator<Listener> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().onError(cause, session);
		}
	}
	
}
