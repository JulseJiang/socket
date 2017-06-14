package mySocketUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public class MyLogin extends JFrame {
	
	private JPanel j_name;
	private JTextField name;
	private JPasswordField password;
	private JButton btn_login;
	private JButton btn_regist;
	private JLabel lab_name;
	private JLabel lab_password;
	private JPanel j_pwd;
	private JPanel j_confi;
	private JPanel room;
	
//	private static String username;
//	private static String pwd;
//	private static String driverName;//驱动名字
//	private static String url="jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true";
	private Font font=new Font("方正舒体", Font.PLAIN, 20);
	
	private String u_name;
	private String u_pwd;
	private int u_id;
	
	private Dao db;
	public MyLogin() {
		this.setTitle("登录");
		this.setFont(font);
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		room = new JPanel();
		init();
		this.add(new MyPanel());
		this.getContentPane().add(room,BorderLayout.SOUTH);
//		this.getContentPane().add(new MyPanel(),BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	private void init(){
		j_name = new JPanel();
		j_pwd = new JPanel();
		j_confi = new JPanel();
		
		name = new JTextField(10);
		name.setText("key");
		password = new JPasswordField(10);
		password.setText("321");
		
		
		lab_name = new JLabel("账户名");
		lab_password = new JLabel("密码");
		
		btn_login = new JButton("登录");
		btn_regist = new JButton("注册");
		
		j_name.add(lab_name);
		j_name.add(name);
		
		j_pwd.add(lab_password);
		j_pwd.add(password);
		
		j_confi.add(btn_login);
		j_confi.add(btn_regist);	
		
		room.add(j_name);
		room.add(j_pwd);
		room.add(j_confi);
		db=new Dao();
		
		btn_login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				u_name=name.getText();
				u_pwd=new String(password.getPassword());
				System.out.println("name="+u_name);
				System.out.println("password="+u_pwd);
				if (db.find(u_name, u_pwd)) {
					JOptionPane.showMessageDialog(null, "登录成功","提示",JOptionPane.INFORMATION_MESSAGE);
					MyLogin.this.setVisible(false);
				}else {
					JOptionPane.showMessageDialog(null, "登录失败，请重试","提示",JOptionPane.INFORMATION_MESSAGE);
				}
				
				
			}
		});
		btn_regist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				u_name=name.getText();
				u_pwd=new String(password.getPassword());
				System.out.println("name="+u_name);
				System.out.println("password="+u_pwd);
				if (db.queryuser(u_name)) {
					JOptionPane.showMessageDialog(null, "该用户名已存在","提示",JOptionPane.INFORMATION_MESSAGE);
				}
				else if (db.insertUser(u_name, u_pwd)) {
					JOptionPane.showMessageDialog(null, "注册成功","提示",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	
	}
	private class MyPanel extends JPanel{

		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			BufferedImage bugi=null;
			try {
				bugi= ImageIO.read(new File("cool.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(bugi, 0, 0, null);
		}
	}
	public String getU_name() {
		return u_name;
	}
	public int getU_id() {
		return u_id;
	}
	
	public static void main(String[] args) {
		MyLogin login = new MyLogin();
		login.setVisible(true);
	}
	
}
