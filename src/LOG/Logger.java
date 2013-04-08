package LOG;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class Logger {
	public static  Connection con;
	private static String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/errorlog";
	private static String MYSQL_USER = "root";
	private static String MYSQL_PASS = "root";
	private static String MYSQL_LOG_QUERY = 
			"insert into log (hashcode,content,class) values(?,?,?)";
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(MYSQL_URL,MYSQL_USER,MYSQL_PASS);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	public static void writelog(Exception e){
		PreparedStatement sta = null;
		try {
			sta = con.prepareStatement(MYSQL_LOG_QUERY);
			
			sta.setInt(1, e.hashCode());
			sta.setString(2, e.toString());
			sta.setString(3, e.getClass().getName());
			sta.executeUpdate();
			sta.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
