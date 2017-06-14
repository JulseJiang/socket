/**
 * @author: Julse
 * @date 2017年5月9日 下午5:17:57
 * @version V1.0
 */
package mySocketUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketTest_ForUIfile {
	
	private SocketUI socketUI;
	private Socket client;
	public ClientSocketTest_ForUIfile(SocketUI socketUI) {
		this.socketUI = socketUI;
		client = socketUI.getSocket_file();
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
			if(this.isAlive()){
				System.out.println("ClientSocketTest_ForUIfile 下载文件");
				DownLoadFile(s);
			}
		}
	}
//	上传文件
	public void UpLoadFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			PrintWriter pw = new PrintWriter(client.getOutputStream());
			pw.println(fis);
			pw.flush();
			fis.close();
			pw.close();
			System.out.println("上传完毕");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	下载文件
	public void DownLoadFile(Socket s){
		System.out.println("文件正在被下载：DownLoadFile");
		File file = new File("E:\\temp.png");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			InputStream in = s.getInputStream();
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				fos.write(buffer);
			}
			in.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
