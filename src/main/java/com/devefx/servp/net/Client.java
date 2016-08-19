package com.devefx.servp.net;

public interface Client {
	
	void connect(String hostname, int port);
	
	void addListener(Listener listener);
}
