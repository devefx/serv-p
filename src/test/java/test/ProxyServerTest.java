package test;

import com.devefx.servp.net.Client;
import com.devefx.servp.net.Server;
import com.devefx.servp.net.Session;
import com.devefx.servp.net.tcp.TcpClient;
import com.devefx.servp.net.tcp.TcpListenerAdapter;
import com.devefx.servp.net.tcp.TcpServer;

/**
 * �������������
 * @author devefx
 * @date 2016-08-19
 */
public class ProxyServerTest {
	
	static final String PROXY = "proxy";
	
	public static void main(String[] args) throws Exception {
		// ����һ��tcp������
		Server server = new TcpServer();
		server.addListener(new ServerListener());
		server.start(9999);
	}
	
	static class ServerListener extends TcpListenerAdapter {
		
		@Override
		public void onCreated(Session session) {
			// ����һ������ͻ���
			Client client = new TcpClient();
			client.addListener(new ClientListener(session));
			// ���ӵ�Ŀ�������
			client.connect("127.0.0.1", 3306);
		}
		@Override
		public void onMessage(Object message, Session session) {
			Session proxySession = session.getAttr(PROXY);
			// ���ô���ͻ��˷�����Ϣ��Ŀ�������
			proxySession.write(message);
			proxySession.flush();
		}
		@Override
		public void onClose(Session session) {
			Session proxySession = session.getAttr(PROXY);
			// �رմ��������
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
			// ����ͻ������ӷ������ɹ�
			originSession.setAttr(PROXY, session);
		}
		@Override
		public void onMessage(Object message, Session session) {
			// ת����Ϣ��ԭʼ�ͻ���
			originSession.write(message);
			originSession.flush();
		}
		@Override
		public void onClose(Session session) {
			// �ر�ԭʼ�ͻ�������
			originSession.close();
		}
	}
}
