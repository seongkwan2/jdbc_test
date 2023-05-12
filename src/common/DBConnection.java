package common;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	public static Connection getConnection() throws Exception{

		//로그인 공통파일
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String id = "C##java" , pwd = "1234";
		String url = "jdbc:oracle:thin:@localhost:1521/xe";
		Connection con = DriverManager.getConnection(url, id, pwd);
		return con;

	}
}
