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
			// ����һ������ͻ���
			Client client = new TcpClient();
			// ���ӵ�Ŀ�������
			client.connect("127.0.0.1", 3306, new ClientHandlerAdapter(session));
		}

		@Override
		public void onSessionMessage(Object message, Session session) {
			
			Session proxySession = session.getAttr("proxySession");
			// ���ô���ͻ��˷�����Ϣ��Ŀ�������
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
			// ����ͻ������ӷ������ɹ�
			originSession.setAttr("proxySession", session);
			
			System.out.println("���ӷ������ɹ�");
		}

		@Override
		public void onSessionMessage(Object message, Session session) {
			// ת����Ϣ��ԭʼ�ͻ���
			originSession.write(message);
			originSession.flush();
		}
		
	}
}
