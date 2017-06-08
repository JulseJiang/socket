/**
 * @author: Julse
 * @date 2017年5月18日 下午4:03:01
 * @version V1.0
 */
package mySocketUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.stream.FileImageOutputStream;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class SocketUI extends JFrame{
	private JPanel p_north;
	private JLabel l_ip;
	private JLabel l_port;
	private JTextField text_ip;
	private JTextField text_port;
	private JButton btn_conn,btn_off;
	
	private JScrollPane p_aside;
	private JList<String> list_people_online=new JList<>();
	
	private JScrollPane p_center;
	private JTextPane jta_content=new JTextPane();
	private StyledDocument sdoc;
//	private JTextArea jArea;
	private StringBuffer str_content;
	
	private JPanel p_south;
	private JLabel l_name;
	private JTextField text_name;
	private JTextField text_buffer;
	private JButton btn_send;
	private JButton btn_img=new JButton("image");
	
	private Socket client;
	private PrintWriter pw;
	private boolean isMass=true;
	private ArrayList<String> ipList=new ArrayList<>();
	private ArrayList<String> namelist=new ArrayList<>();
	public SocketUI() {
		this.setTitle("聊天室");
		this.setSize(800,600);
		
		this.setLocationRelativeTo(null);
		init();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(p_north, BorderLayout.NORTH);
		this.getContentPane().add(p_aside, BorderLayout.EAST);
		this.getContentPane().add(p_south, BorderLayout.SOUTH);
		this.getContentPane().add(p_center, BorderLayout.CENTER);
	}
	public SocketUI(String title){
		this();
		this.setTitle(title);
		this.getContentPane().remove(p_aside);
		this.getContentPane().remove(p_north);
		p_north=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p_north.add(btn_off);
		this.getContentPane().add(p_north, BorderLayout.NORTH);
	}
	private void init() {
				
		
		
//		北方--ip和端口
		l_ip=new JLabel("ip:");
		l_port=new JLabel("port:");
		text_ip=new JTextField(20);
		text_ip.setText("localhost");
//		text_ip.setText("100.0.101.15");
		text_port=new JTextField(15);
		text_port.setText("20000");
		btn_conn=new JButton("Connect");
		btn_off=new JButton("disconnect");
		p_north=new JPanel();
		p_north.add(l_ip);
		p_north.add(text_ip);
		p_north.add(l_port);
		p_north.add(text_port);
		p_north.add(btn_conn);
		p_north.add(btn_off);
//		侧栏--联系人列表
		p_aside=new JScrollPane(list_people_online);
		
//		中心显示窗口
//		jArea=new JTextArea();
		jta_content.setEditable(false);
		p_center=new JScrollPane(jta_content);
//		jlist的使用方法
		
//		南方--发送窗口
		l_name=new JLabel("nick name:");
		text_name=new JTextField(8);
		text_name.setText("soman");
		text_buffer =new JTextField(50);
		text_buffer.setText("开心就好");
		btn_send=new JButton("Send");
		
		p_south=new JPanel();
		p_south.add(l_name);
		p_south.add(text_name);
		p_south.add(text_buffer);
		p_south.add(btn_send);
		p_south.add(btn_img);
		
		ConnState(false);
		
		btn_conn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ToConn();
				
			}
		});
		btn_off.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DisConn();
				
				
				
			}
		});
		btn_send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ("".equals(text_name.getText().trim()) || "".equals(text_buffer.getText().trim())) {
					SocketUI.this.setStr_content("昵称或发送内容不能为空");
				} 
				else if (client != null && !client.isClosed()) {
					
					String str =text_name.getText().trim()+":"+ text_buffer.getText().trim();
					//						pw.println(text_name.getText().trim());
					//						pw.println(text_buffer.getText().trim());
											pw.println(str);
											pw.flush();
					//						text_buffer.setText("");
					//						setStr_content(str);
				}

			}
		});
		btn_img.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser=new JFileChooser("E:\\img\\qq表情");
				chooser.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg","png","gif","jpeg"));
				if (chooser.showOpenDialog(SocketUI.this)==JFileChooser.APPROVE_OPTION) {
					File file=chooser.getSelectedFile();
					setIcon_content(file.getAbsolutePath());
				}
				
			}
		});
		list_people_online.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount()==2&&list_people_online.getSelectedIndex()!=0) {
					String item=list_people_online.getSelectedValue();
					new SocketUI(item).setVisible(true);
				}
			}
		});

	}
	public void ConnState(boolean flag){
	
			btn_conn.setEnabled(!flag);
			btn_img.setEnabled(flag);
			btn_send.setEnabled(flag);
			btn_off.setEnabled(flag);
		
	}
	
	
	public void refreshClientList(ArrayList cList){
		ClientListModel model=new ClientListModel(cList);
		list_people_online.setModel(model);
		namelist=cList;
		
	}
	class ClientListModel extends DefaultListModel{
		private ArrayList list;
		public ClientListModel(ArrayList list) {
			this.list=list;
		}
		@Override
		public int getSize() {
			// TODO Auto-generated method stub
			return list.size();
		}
		@Override
		public Object getElementAt(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}
		
	}
	public void DisConn(){
//		int result =JOptionPane.showConfirmDialog(SocketUI.this, "退出聊天室");
		
//		if (result==0) {
			try {
				pw.println("quit");
				pw.flush();
				client.close();
				ConnState(false);
				setStr_content("退出聊天室");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
	}
	public void ToConn(){
		try {
			client=new Socket(text_ip.getText().trim(),Integer.parseInt(text_port.getText().trim()));
			if (client.isConnected()) {
				sdoc=jta_content.getStyledDocument();
				ConnState(true);
				pw = new PrintWriter(client.getOutputStream());
				
				
				new ClientSocketTest_ForUI(SocketUI.this);
			}
			else {
//				str_content.append("连接失败");
				setStr_content("fail to connect");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setIpList(ArrayList<String> ipList) {
		this.ipList = ipList;
	}
	public void setStr_content(String str) {
//		this.str_content = this.str_content.append(str+"\n");
//		jArea.setText(str_content.toString());
		try {
			sdoc.insertString(sdoc.getLength(), str+"\n", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setIcon_content(String url){
		Icon icon=new ImageIcon(url);
		jta_content.insertIcon(icon);
		
		
		
	}
//	public String getMes(){
//		return text_buffer.getText().trim();
//	}
	public Socket getClient() {
		return client;
	}
	
	public String getIp() {
		return l_ip.getText().trim();
	}
	public String getPort(){
		return l_port.getText().trim();
	}
	public void setClient(Socket client) {
		this.client = client;
	}
	@Override
	public int getDefaultCloseOperation() {
		if (client!=null) {
			DisConn();
		}
		
		return super.getDefaultCloseOperation();
	}
	public void sentFile(File file){
		try {
			FileInputStream fis= new FileInputStream(file);
			Socket socket = new Socket(text_ip.getText().toString(), Integer.parseInt(text_port.getText().toString())+1);
			OutputStream out = socket.getOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len=fis.read(buffer))!=-1){
				out.write(buffer,0,len);
			}
			socket.shutdownInput();
//			out.close();
//			socket.close();
//			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void recieve(File file){
		
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//适应系统的显示风格
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new SocketUI().setVisible(true);
	}
}
