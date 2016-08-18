package com.devefx.servp.net;

public interface HandlerAdapter {
	
	void onSessionCreated(Session session);
	
	void onSessionMessage(Object message, Session session);
	
}
