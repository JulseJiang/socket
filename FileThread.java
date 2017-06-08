/**
 * @author: Julse
 * @date 2017年6月8日 下午5:35:08
 * @version V1.0
 */
package mySocketUI;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileThread implements Runnable{
	private Socket s;
	public FileThread(Socket s) {
		this.s= s;
	}

	@Override
	public void run() {
		int count = 1 ;
		String ip = s.getInetAddress().getHostAddress();
		
		try {
			InputStream in = s.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
