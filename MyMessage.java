/**
 * @author: Julse
 * @date 2017年6月3日 下午2:30:45
 * @version V1.0
 */
package mySocketUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyMessage {
	private String from,to,content,dateTime;
	private boolean ifMass;
	private int mType;
	private ArrayList<String> cList;
	private SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	public MyMessage() {
		dateTime=sdf.format(System.currentTimeMillis());
	}
	public MyMessage(int mType){
		this();
		this.mType=mType;
		
	}
	public MyMessage(boolean ifMass,String from,String to,String content){
		this();
		this.ifMass = ifMass;
		this.from = from;
		this.to = to;
		this.content = content;
	}
	
}
