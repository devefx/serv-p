package com.devefx.servp.net;

public interface Server {
	
	void start(String hostname, int port, HandlerAdapter adapter) throws Exception;
	
	void start(int port, HandlerAdapter adapter) throws Exception;
}
