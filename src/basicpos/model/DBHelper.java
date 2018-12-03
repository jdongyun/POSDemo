package basicpos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
	public final static String dbName = "ProductDB.db";
	private static Connection connect;
	
	public static Connection connect() {
		// SQLite connection string
		String url = "jdbc:sqlite:" + dbName;
		
		try {
			if(connect == null)
				connect = DriverManager.getConnection(url);
			if(connect.isClosed())
				connect = DriverManager.getConnection(url);
				//connect.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return connect;
	}
	
	public static void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
}
