package application2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/rekam_medik";
	private static final String USER = "root";
	private static String PASSWORD = "root";
	
	public static Connection getConnection()throws SQLException{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

}