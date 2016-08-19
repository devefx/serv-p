package com.devefx.servp.net;

public interface Server {
	
	void start(String hostname, int port) throws Exception;
	
	void start(int port) throws Exception;
	
	void addListener(Listener listener);
}
