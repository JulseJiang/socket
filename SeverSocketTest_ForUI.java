/**
 * @author: Julse
 * @date 2017年5月9日 下午5:19:45
 * @version V1.0
 */
package mySocketUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SeverSocketTest_ForUI {
	ServerSocket ss;
	private ArrayList<String> clientlist = new ArrayList<>();
	private ArrayList<String> namelist = new ArrayList<>();
	private ArrayList<PrintWriter> pwlist = new ArrayList<>();
	private ArrayList<BufferedReader> brlist = new ArrayList<>();
	private LinkedList<String> msglist = new LinkedList<>();
	private String ip;
	private int port;
	public SeverSocketTest_ForUI() {
		try {
			ss = new ServerSocket(20000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new ClientThread().start();
		new SendMsgToAllClient().start();
	}
//	接收客户端套接字线程
	class ClientThread extends Thread {
	@Override
		public void run() {
			namelist.add("namelist===");
			clientlist.add("iplist===");
			while(this.isAlive()){
				BufferedReader buf = null;
				try {
					Socket s=ss.accept();
					clientlist.add(s.getInetAddress().toString());
					namelist.add("小明");
					System.out.println(s.getInetAddress());

					buf = new BufferedReader(new InputStreamReader(s.getInputStream()));
					brlist.add(buf);
					new GetMsgFromClient(buf).start();
					PrintWriter pw = new PrintWriter(s.getOutputStream());
					pwlist.add(pw);
					pw.println("连接成功  " + "在线人数：" + (clientlist.size()-1));
					pw.println("欢迎来到聊天室");
					pw.println(namelist);
					pw.flush();
				} catch (SocketException e) {
					System.out.println("socket已关闭");
					e.printStackTrace();
					// 循环结束之后，或者连接已关闭的抛异常，就执行finally
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// System.out.println("run finally:"+s.getLocalAddress());
					//
					// System.out.println("关闭一个client后"+brlist.size());
					System.out.println("获取客户端通讯通道已打开  " + clientlist.size());
				}
			}
			

		}

		public void sendClientList() {
			// Set<String> keySet=clientMap.keySet();
			// Iterator<String > it=keySet.iterator();
			// while(it.hasNext()){
			// String ip=it.next();
			// Socket s=clientMap.get(ip);
			// SendMes(s,getClientList());
			// }
//			SendMes(s, clientlist);
		}

		// public ArrayList getClientList(){
		// ArrayList<String> clientList = new ArrayList<>();
		// clientList.add("我是ip地址");
		// Set<String> keySet=clientMap.keySet();
		// for (String str : keySet) {
		// clientList.add(str);
		// }
		// return clientList;
		// }
		private void SendMes(Socket s, Object mes) {

			try {
				// ObjectOutputStream out=new
				// ObjectOutputStream(s.getOutputStream());
				// out.writeObject(mes);
				// PrintWriter pw=new PrintWriter(out);
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				pw.println(mes);
				pw.flush();
				// pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 从各个客户端获取消息
	class GetMsgFromClient extends Thread {
		BufferedReader buf;

		public GetMsgFromClient(BufferedReader buf) {
			this.buf = buf;
		}

		@Override
		public void run() {
			int key=brlist.indexOf(buf);
			try {
				while (this.isAlive()) {
					String str=buf.readLine();
					if (str.equals("quit")){
						 
						 System.out.println("key:"+key);
						 clientlist.remove(key+1);
						 pwlist.remove(key);
						 brlist.remove(key);
						 namelist.remove(key+1);
					}
					else if (str!= null){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = format.format(new Date());
						msglist.addFirst("<==" + time + "==>   " + str);
						namelist.set(key+1, str.substring(0, str.indexOf(":")));
						
					}
					

				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				System.out.println("GetMsgFromClient 结束");
				
			}

			super.run();
		}
	}

	// 群发消息给各个客户端
	class SendMsgToAllClient extends Thread {
		
		@Override
		public void run() {
			while (this.isAlive()) {
				if (!msglist.isEmpty()) {
					// String msg=msglist.remove();
					String msg = msglist.removeLast();
					for (int i = 0; i < pwlist.size(); i++) {
						pwlist.get(i).println(msg);
						pwlist.get(i).println(clientlist);
						pwlist.get(i).println(namelist);
						pwlist.get(i).flush();

					}
				}
			}
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());// 适应系统的显示风格
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new SocketUI().setVisible(true);
		new SocketUI().setVisible(true);
		new SocketUI().setVisible(true);
		new SeverSocketTest_ForUI();
	}

}
