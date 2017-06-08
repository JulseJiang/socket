/**
 * @author: Julse
 * @date 2017年5月9日 下午5:17:57
 * @version V1.0
 */
package mySocketUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.TextUI;

public class ClientSocketTest_ForUI {
	private SocketUI socketUI;
	private Socket client;
	// private String ip;
	// private String port;
	public ClientSocketTest_ForUI(SocketUI socketUI) {
		this.socketUI = socketUI;
		// ip=socketUI.getIp();
		// port=socketUI.getPort();
		client = socketUI.getClient();
		new SeverThread(client).start();
	}

	class SeverThread extends Thread {
		Socket s;

		public SeverThread(Socket s) {
			this.s = s;
		}

		@Override
		public void run() {
			super.run();
//			GetText(s);
			GetMes(s);

		}
	}
	public void GetText(Socket s){
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str;
			while (!("quit".equals(str = buf.readLine()))) {
				if (Pattern.compile("iplist===").matcher(str).find()) {
					ArrayList<String> cList=new ArrayList<>();
					System.out.println(str);
					 String ke=str.substring(str.indexOf("iplist==="),str.length()-1);
					 String[] strs=ke.split(", /");
					 for (String string : strs) {
						System.out.println(string);
						cList.add(string);
					}
//					 cList.set(0, "成员列表："+(strs.length-1)+"人");
					 socketUI.setIpList(cList);
				}else if (Pattern.compile("namelist===").matcher(str).find()) {
					ArrayList<String> cList=new ArrayList<>();
					System.out.println(str);
					 String ke=str.substring(str.indexOf("namelist==="),str.length()-1);
					 String[] strs=ke.split(",");
					 for (String string : strs) {
						System.out.println(string);
						cList.add(string);
					}
					 cList.set(0, "成员列表："+(strs.length-1)+"人");
					 socketUI.refreshClientList(cList);
				}
				else {
					socketUI.setStr_content(str);
				}
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("buf:" + "异常");
		} finally {
			try {
				buf.close();
				System.out.println("buf:" + "关闭");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void GetMes(Socket s) {
		Object mes = null;
		try {
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			mes = in.readObject();
			if (mes != null) {
				if (mes instanceof String) {
					socketUI.setStr_content(mes.toString());
				}
//				if (mes instanceof ArrayList) {
//					ArrayList personlist = (ArrayList) mes;
//					for (Object object : personlist) {
//						System.out.println(object.toString());
//					}
//				}
			}
			if (mes instanceof String) {
				socketUI.setStr_content(mes.toString());
			}
			if (mes instanceof ArrayList) {
				ArrayList personlist = (ArrayList) mes;
				for (Object object : personlist) {
					System.out.println(object.toString());
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return (ArrayList<String>) mes;
	}
	
	public void GetFile(Socket s){
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientSocketTest_ForUI(new SocketUI());
	}

}
