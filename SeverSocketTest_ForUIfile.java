/**
 * @author: Julse
 * @date 2017年5月9日 下午5:19:45
 * @version V1.0
 */
package mySocketUI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SeverSocketTest_ForUIfile {
	ServerSocket ss;
	private ArrayList<String> clientlist = new ArrayList<>();
	private ArrayList<String> namelist = new ArrayList<>();
	private ArrayList<PrintWriter> pwlist = new ArrayList<>();
	private ArrayList<InputStreamReader> iReaderlist = new ArrayList<>();
	private LinkedList<String> msglist = new LinkedList<>();
	public SeverSocketTest_ForUIfile() {
		try {
			ss = new ServerSocket(20001);
			System.out.println("SeverSocketTest_ForUIfile:文件传输通道已打开");
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
				
				try {
					Socket s=ss.accept();
					System.out.println("小明你好，现在下载文件");
					clientlist.add(s.getInetAddress().toString());
					namelist.add("小明");
					System.out.println(s.getInetAddress());

//					buf = new BufferedReader(new InputStreamReader(s.getInputStream()));
//					brlist.add(buf);
//					new GetMsgFromClient(buf).start();
					InputStreamReader iReader = new InputStreamReader(s.getInputStream());
					iReaderlist.add(iReader);
					new GetMsgFromClient(iReader).start();
					PrintWriter pw = new PrintWriter(s.getOutputStream());
					pwlist.add(pw);
					
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

	// 从各个客户端读取文件
	class GetMsgFromClient extends Thread {
		InputStreamReader iReader= null;

		public GetMsgFromClient(InputStreamReader iReader) {
			this.iReader=iReader;
		}

		@Override
		public void run() {
			int key=iReaderlist.indexOf(iReader);
			try {
				while (this.isAlive()) {
					
					
					

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
				if (iReaderlist.size()>0) {
					// String msg=msglist.remove();
//					String msg = msglist.removeLast();
					for(int j = 0;j<iReaderlist.size();j++){
						for (int i = 0; i < pwlist.size(); i++) {
							pwlist.get(i).println(iReaderlist.get(j));
//							pwlist.get(i).println(clientlist);
//							pwlist.get(i).println(namelist);
							pwlist.get(i).flush();

						}
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
		new SeverSocketTest_ForUIfile();
	}

}
