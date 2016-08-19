package test;

import com.devefx.servp.net.Client;
import com.devefx.servp.net.Server;
import com.devefx.servp.net.Session;
import com.devefx.servp.net.tcp.TcpClient;
import com.devefx.servp.net.tcp.TcpListenerAdapter;
import com.devefx.servp.net.tcp.TcpServer;

/**
 * 代理服务器案例
 * @author devefx
 * @date 2016-08-19
 */
public class ProxyServerTest {
	
	static final String PROXY = "proxy";
	
	public static void main(String[] args) throws Exception {
		// 创建一个tcp服务器
		Server server = new TcpServer();
		server.addListener(new ServerListener());
		server.start(9999);
	}
	
	static class ServerListener extends TcpListenerAdapter {
		
		@Override
		public void onCreated(Session session) {
			// 创建一个代理客户端
			Client client = new TcpClient();
			client.addListener(new ClientListener(session));
			// 连接到目标服务器
			client.connect("127.0.0.1", 3306);
		}
		@Override
		public void onMessage(Object message, Session session) {
			Session proxySession = session.getAttr(PROXY);
			// 利用代理客户端发送消息到目标服务器
			proxySession.write(message);
			proxySession.flush();
		}
		@Override
		public void onClose(Session session) {
			Session proxySession = session.getAttr(PROXY);
			// 关闭代理服务器
			proxySession.close();
		}
	}
	
	static class ClientListener extends TcpListenerAdapter {

		Session originSession;
		
		public ClientListener(Session session) {
			this.originSession = session;
		}
		@Override
		public void onCreated(Session session) {
			// 代理客户端连接服务器成功
			originSession.setAttr(PROXY, session);
		}
		@Override
		public void onMessage(Object message, Session session) {
			// 转发消息到原始客户端
			originSession.write(message);
			originSession.flush();
		}
		@Override
		public void onClose(Session session) {
			// 关闭原始客户端连接
			originSession.close();
		}
	}
}
