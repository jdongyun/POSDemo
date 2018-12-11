package basicpos.model;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProductHelper {
	
	public static boolean insertProduct(Product product) {
		String query = "INSERT INTO `Product`(`ProductCode`, `ProductName`, `ProductPrice`, `IsAdultOnly`, `ProductRemain`) "
				+ "VALUES(?,?,?,?,?)";

		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, product.getProductCode());
			pstmt.setString(2, product.getProductName());
			pstmt.setInt(3, product.getProductPrice());
			pstmt.setInt(4, product.getIsAdultOnly() ? 1 : 0);
			pstmt.setInt(5, product.getProductRemain());
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			//System.out.println(e.getMessage());
			DBHelper.close();
			return false;
		}
	}
	
	public static boolean updateProduct(Product product) {
		String query = "UPDATE `Product` SET `ProductName`=?, `ProductPrice`=?, "
				+ "`IsAdultOnly`=?, `ProductRemain`=? WHERE `ProductCode`=?";
		
		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, product.getProductName());
			pstmt.setInt(2, product.getProductPrice());
			pstmt.setInt(3, product.getIsAdultOnly() ? 1 : 0);
			pstmt.setInt(4, product.getProductRemain());
			pstmt.setInt(5, product.getProductCode());
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			DBHelper.close();
			return false;
		}
	}
	
	public static boolean deleteProduct(int prodCode) {
		String query = "DELETE FROM `Product`"
				+ "WHERE `ProductCode`=?";

		try {
			Connection conn = DBHelper.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, prodCode);
			pstmt.executeUpdate();
			DBHelper.close();
			return true;
		} catch (SQLException e) {
			//System.out.println(e.getMessage());
			DBHelper.close();
			return false;
		}
	}
	
	public static Product getProduct(int productCode) {
		String query = "SELECT * FROM Product where ProductCode=" + productCode;

		try {
			Connection conn = DBHelper.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			Product p = new Product(productCode, rs.getString("ProductName"), 
						1, rs.getInt("ProductPrice"), (rs.getInt("IsAdultOnly") == 1),
						rs.getInt("ProductRemain"));
			DBHelper.close();
			return p;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Iterator<Product> getProductList() {
		String query = "SELECT * FROM Product";
		List<Product> list = new LinkedList<Product>();

		try {
			Connection conn = DBHelper.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// loop through the result set
			while (rs.next()) {
				Product tempProduct = new Product();
				tempProduct.setProductCode(rs.getInt("ProductCode"));
				tempProduct.setProductName(rs.getString("ProductName"));
				tempProduct.setProductPrice(rs.getInt("ProductPrice"));
				tempProduct.setIsAdultOnly(rs.getInt("IsAdultOnly") == 1);
				tempProduct.setProductRemain(rs.getInt("ProductRemain"));

				list.add(tempProduct);
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

		// SQL statement for creating a new table
		String sql = "CREATE TABLE `Product` (\n" + " `ProductCode` integer UNIQUE,\n"
				+ " `ProductName` text NOT NULL,\n" + " `ProductPrice` integer NOT NULL,\n"
				+ " `IsAdultOnly` integer NOT NULL,\n" + " `ProductRemain` integer NOT NULL\n"+ ");";

		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
}
