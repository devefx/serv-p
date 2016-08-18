package com.devefx.servp.net;

import io.netty.channel.ChannelHandlerContext;

public interface Session {
	
	public String getId();
	
	public ChannelHandlerContext getChannelHandlerContext();
	
	public String getRemoteAddress();
	
	public String getLocalAddr();
	
	public<V> V getAttr(String key);
	
	public<V> void setAttr(String key, V value);
	
	public void write(Object msg);
	
	public void flush();
}
