package basicpos.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
	public final static String dbName = "ProductDB.db";
	private static Connection connect;
	
	//새로운 연결 생성
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
	
	//연결 닫기
	public static void close() {
		try {
			connect.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	//전체 데이터베이스 생성
	public static void createNewDatabase() {
		String url = "jdbc:sqlite:" + DBHelper.dbName;
		try {
			Connection conn = DriverManager.getConnection(url);
			if (conn != null) {
				//DatabaseMetaData meta = conn.getMetaData();
				//System.out.println("The driver name is " + meta.getDriverName());
				//System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
