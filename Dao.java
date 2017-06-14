/**
 * @author: Julse
 * @date 2017年6月14日 下午11:20:05
 * @version V1.0
 */
package mySocketUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Dao {
	private Connection conn;
	public Dao() {
		conn=DBUtil.getConn();
	}
	
	public boolean find(String username,String pwd){
		/*String sql="select * from userInfo where userName="+"'"+name.getText().trim()+"'";
		 * //漏洞：易引发脚本注入攻击 name：xx' or '1'='1,password:xx' or '1'='1
		System.out.println(sql);*/
		String sql="select * from userInfo where userName=? and password=?;";
		//预编译，等待补全不完整部分，效率高，大部分已经提前编译好，一定程度上能防止脚本攻击
		try {
			PreparedStatement pstmt=null;
			
			/*Statement stm= conn.createStatement();
		    ResultSet rs =stm.executeQuery(sql);*/
		    
		    pstmt=conn.prepareStatement(sql);
		    pstmt.setString(1,username);
			pstmt.setString(2, pwd);
			ResultSet rs=pstmt.executeQuery();
			return rs.next();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			//DBUtil.close();
		}
		return false;
	}
	public boolean insertUser(String name,String pwd){
		String sql="insert into userinfo (username,password)VALUES("+ "'"+name+"',"+pwd+");";
		PreparedStatement pstmt=null;
	    try {
			pstmt=conn.prepareStatement(sql);
			pstmt.execute(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   	
		return false;
	}
	public boolean queryuser(String name){
		String sql="select * from userInfo where userName=?;";
		PreparedStatement pstmt=null;
	    
	    try {
			pstmt=conn.prepareStatement(sql);
			   pstmt.setString(1,name);
				ResultSet rs=pstmt.executeQuery();
				System.out.println(rs!=null);
				return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		return false;
	}
}
