/**
 * 读取聊天记录
 * @author: Julse
 * @date 2017年5月9日 下午5:17:57
 * @version V1.0
 */
package mySocketUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ClientSocketTest_ForUI {
	private SocketUI socketUI;
	private Socket client;

	public ClientSocketTest_ForUI(SocketUI socketUI) {
		System.out.println("ClientSocketTest_ForUI 客户端消息处理器已启动");
		this.socketUI = socketUI;
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
			 GetText(s);
			 
		}
	}

	public void GetText(Socket s) {
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str;
			while (!("quit".equals(str = buf.readLine()))) {
				System.out.println("ClientSocketTest_ForUI GetText 正在读取消息...");
				if (Pattern.compile("iplist===").matcher(str).find()) {
					ArrayList<String> cList = new ArrayList<>();
					System.out.println(str);
					String ke = str.substring(str.indexOf("iplist==="), str.length() - 1);
					String[] strs = ke.split(", /");
					for (String string : strs) {
						System.out.println(string);
						cList.add(string);
					}
					// cList.set(0, "成员列表："+(strs.length-1)+"人");
					socketUI.setIpList(cList);
				} else if (Pattern.compile("namelist===").matcher(str).find()) {
					ArrayList<String> cList = new ArrayList<>();
					System.out.println(str);
					String ke = str.substring(str.indexOf("namelist==="), str.length() - 1);
					String[] strs = ke.split(",");
					for (String string : strs) {
						System.out.println(string);
						cList.add(string);
					}
					cList.set(0, "成员列表：" + (strs.length - 1) + "人");
					socketUI.refreshClientList(cList);
				} else {
					if (Pattern.compile("file===").matcher(str).find()) {
						String ke = str.substring(str.indexOf("file==="), str.length() - 1);
						socketUI.setStr_content(ke);
					}
					else {
						socketUI.setStr_content(str);
					}
					
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("buf:" + "异常");
		} finally {
			try {
				if (buf!=null) {
					buf.close();
				}
				
				System.out.println("buf:" + "关闭");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
