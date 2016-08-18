package com.devefx.servp.net.tcp;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.ChannelHandlerContext;

import com.devefx.servp.net.Session;

@SuppressWarnings("unchecked")
public class TcpSession implements Session {

	private final String id;
	private ChannelHandlerContext context;
	private ConcurrentMap<String, Object> attrs;
	
	public TcpSession(ChannelHandlerContext context) {
		this.id = UUID.randomUUID().toString();
		this.context = context;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public ChannelHandlerContext getChannelHandlerContext() {
		return context;
	}
	
	@Override
	public String getRemoteAddress() {
		return context.channel().remoteAddress().toString();
	}
	
	@Override
	public String getLocalAddr() {
		return context.channel().localAddress().toString();
	}

	@Override
	public <V> V getAttr(String key) {
		if (attrs != null) {
			return (V) attrs.get(key);
		}
		return null;
	}

	@Override
	public <V> void setAttr(String key, V value) {
		if (value == null) {
			if (attrs != null) {
				attrs.remove(key);
			}
		} else {
			if (attrs == null) {
				synchronized (this) {
					attrs = new ConcurrentHashMap<>();
				}
			}
			attrs.put(key, value);
		}
	}

	@Override
	public void write(Object msg) {
		context.channel().write(msg);
	}

	@Override
	public void flush() {
		context.channel().flush();
	}
}
