package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/jual_beli_mobil";
	private static final String USER = "root";
	private static String PASSWORD = "root";
	
	public static Connection getConnection()throws SQLException{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

}