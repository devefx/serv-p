package com.devefx.servp.net;

public interface Listener {
	
	void onCreated(Session session);
	
	void onMessage(Object message, Session session);
	
	void onClose(Session session);
	
	void onError(Throwable cause, Session session);
}
