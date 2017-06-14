
package mySocketUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	private static String databasename="test";
	private static String username="root";
	private static String pwd = "123";
	private static String driverName="com.mysql.jdbc.Driver";//驱动名字
	//private static String url="jdbc:mysql://localhost:3306/test?useSSL=true";
	//默认打开这个test数据库,参数不能有空格,没有useSSL会报错
//	private static String url="jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true";
//	private static String url="jdbc:mysql://localhost:3306/"+databasename+"?useUnicode=true&characterEncoding=utf-8&useSSL=true";
	private static Connection conn;
	public static Connection getConn(){//注意这个connection导入的包：import java.sql.Connection;
		conn = null;
		String url="jdbc:mysql://localhost:3306/"+databasename+"?useUnicode=true&characterEncoding=utf-8&useSSL=true";
		try {
			Class.forName(driverName);
			try {
				System.out.println("url="+url);
				conn=DriverManager.getConnection(url,username,pwd);//实体类
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//tomcate需要这行
		return conn;
	}
	

	public  static void close(ResultSet rs, Statement stmt, Object object) {
		// TODO Auto-generated method stub

			try {
				if (rs!=null) {
					rs.close();
				}
				if (stmt!=null) {
					stmt.close();
				}
				if (conn!=null) {
					conn.close();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("rs,stmt已关闭");
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(getConn());//测试 ctrl + F11 运行程序的快捷键
	}

public static void setDatabasename(String databasename) {
	DBUtil.databasename = databasename;
	System.out.println("databasename="+databasename);
}


}
