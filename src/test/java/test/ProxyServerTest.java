package test;

import com.devefx.servp.net.Client;
import com.devefx.servp.net.HandlerAdapter;
import com.devefx.servp.net.Server;
import com.devefx.servp.net.Session;
import com.devefx.servp.net.tcp.TcpClient;
import com.devefx.servp.net.tcp.TcpServer;

public class ProxyServerTest {
	
	public static void main(String[] args) throws Exception {
		
		final Server server = new TcpServer();
		
		server.start(9999, new ServerHandlerAdapter());
		
	}
	
	static class ServerHandlerAdapter implements HandlerAdapter {
		
		@Override
		public void onSessionCreated(Session session) {
			// 创建一个代理客户端
			Client client = new TcpClient();
			// 连接到目标服务器
			client.connect("127.0.0.1", 3306, new ClientHandlerAdapter(session));
		}

		@Override
		public void onSessionMessage(Object message, Session session) {
			
			Session proxySession = session.getAttr("proxySession");
			// 利用代理客户端发送消息到目标服务器
			proxySession.write(message);
			proxySession.flush();
		}
		
	}
	
	static class ClientHandlerAdapter implements HandlerAdapter {

		Session originSession;
		
		public ClientHandlerAdapter(Session session) {
			this.originSession = session;
		}
		
		@Override
		public void onSessionCreated(Session session) {
			// 代理客户端连接服务器成功
			originSession.setAttr("proxySession", session);
			
			System.out.println("连接服务器成功");
		}

		@Override
		public void onSessionMessage(Object message, Session session) {
			// 转发消息到原始客户端
			originSession.write(message);
			originSession.flush();
		}
		
	}
}
