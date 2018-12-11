package basicpos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CouponHelper {
	
	//새로운 쿠폰 객체를 데이터베이스에 추가
	public static boolean insertCoupon(Coupon coupon) {
		String query = "INSERT INTO `Coupon`(`CouponCode`, `ProductCode`, `ProductCount`, `DiscountRate`, `IsUsed`) "
				+ "VALUES(?,?,?,?,?)";

		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, coupon.getCouponCode());
			pstmt.setInt(2, coupon.getProductCode());
			pstmt.setInt(3, coupon.getProductCount());
			pstmt.setInt(4, coupon.getDiscountRate());
			pstmt.setInt(5, coupon.getIsUsed() ? 1 : 0);
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			DBHelper.close();
			return false;
		}
	}
	
	//수정된 쿠폰 객체를 데이터베이스에 적용
	public static boolean updateCoupon(Coupon coupon) {
		String query = "UPDATE `Coupon` SET `ProductCode`=?, `ProductCount`=?, "
				+ "`DiscountRate`=?, `IsUsed`=? WHERE `CouponCode`=?";
		
		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, coupon.getProductCode());
			pstmt.setInt(2, coupon.getProductCount());
			pstmt.setInt(3, coupon.getDiscountRate());
			pstmt.setInt(4, coupon.getIsUsed() ? 1 : 0);
			pstmt.setInt(5, coupon.getCouponCode());
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			DBHelper.close();
			return false;
		}
	}
	
	//쿠폰 삭제
	public static boolean deleteCoupon(int couponCode) {
		String query = "DELETE FROM `Coupon`"
				+ "WHERE `CouponCode`=?";

		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, couponCode);
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			DBHelper.close();
			return false;
		}
	}
	
	//쿠폰 객체를 데이터베이스에서 가져오기
	public static Coupon getCoupon(int couponCode) {
		String query = "SELECT * FROM Coupon where CouponCode=" + couponCode;

		try {
			Connection conn = DBHelper.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			Coupon coupon = new Coupon(couponCode, rs.getInt("ProductCode"),
					rs.getInt("ProductCount"),rs.getInt("DiscountRate"),rs.getInt("IsUsed") == 1);
			
			DBHelper.close();
			return coupon;
		} catch (SQLException e) {
			return null;
		}
	}
	
	//전체 쿠폰 목록을 데이터베이스에서 가져오기
	public static Iterator<Coupon> getCouponDB() {
		String query = "SELECT * FROM Coupon";
		List<Coupon> list = new LinkedList<Coupon>();

		try {
			Connection conn = DBHelper.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// loop through the result set
			while (rs.next()) {
				Coupon coupon = new Coupon(rs.getInt("CouponCode"),
											rs.getInt("ProductCode"),
											rs.getInt("ProductCount"),
											rs.getInt("DiscountRate"),
											rs.getInt("IsUsed") == 1);
				list.add(coupon);
			}
			DBHelper.close();
			return list.iterator();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	//쿠폰 테이블 생성
	public static void createNewTable() { // SQLite connection string
		String url = "jdbc:sqlite:" + DBHelper.dbName;
		String sql = "CREATE TABLE `Coupon` (\n" + " `CouponCode` integer UNIQUE,\n"
				+ " `ProductCode` integer NOT NULL,\n"
				+ " `ProductCount` integer NOT NULL,\n"
				+ " `DiscountRate` integer NOT NULL,\n"
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
