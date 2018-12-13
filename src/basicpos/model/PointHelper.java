package basicpos.model;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PointHelper {
	/*
	 * 포인트 적립 및 포인트 사용
	 */
	public static boolean insertPoint(Point point) {
		String query = "INSERT INTO `Point`(`UserCode`, `UserPoint`) " + "VALUES(" + point.getUserCode() + ","
				+ point.getUserPoint() + ")";

		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updatePoint(Point point) {

		String query = "UPDATE `Point` SET "
					+ "`UserPoint`="+ point.getUserPoint() + " WHERE UserCode=" + point.getUserCode();
		
		try  {
			Connection conn = DBHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            DBHelper.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public static Point getPoint(int userCode) {
		String query = "SELECT * FROM Point where UserCode=" + userCode;

		try {
			Connection conn = DBHelper.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(!rs.next()) {
				return null; //userCode에 해당하는 사용자 번호 없음
			}
			int userPoint = rs.getInt("UserPoint");
			DBHelper.close();
			return new Point(userCode, userPoint);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static Iterator<Point> getPointDB() {
		String query = "SELECT * FROM Point";
		List<Point> list = new LinkedList<Point>();

		try {
			Connection conn = DBHelper.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				Point tempPoint = new Point(rs.getInt("UserCode"),
											rs.getInt("UserPoint"));
				list.add(tempPoint);
			}
			DBHelper.close();
			return list.iterator();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static void createNewTable() { // SQLite connection string
		String url = "jdbc:sqlite:" + DBHelper.dbName;
		String sql = "CREATE TABLE `Point` (\n" + " `UserCode` integer UNIQUE,\n"
				+ " `UserPoint` integer NOT NULL\n" + ");";

		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
