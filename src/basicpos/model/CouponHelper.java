package basicpos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponHelper {
	
	

	public static void createNewTable() { // SQLite connection string
		String url = "jdbc:sqlite:" + DBHelper.dbName;
		String sql = "CREATE TABLE `Coupon` (\n" + " `CouponCode` integer UNIQUE,\n"
				+ " `ProductCode` integer NOT NULL\n"
				+ " `IsUsed` integer NOT NULL\n"+ ");";
		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
