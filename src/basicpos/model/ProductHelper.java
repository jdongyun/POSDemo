package basicpos.model;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProductHelper {
	private final static String dbName = "ProductDB.db";

	private Connection connect() {
		// SQLite connection string
		String url = "jdbc:sqlite:" + dbName;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public boolean insertProduct(int prodCode, String prodName, int prodPrice, boolean isAdultOnly) {
		String query = "INSERT INTO `Product`(`ProductCode`, `ProductName`, `ProductPrice`, `IsAdultOnly`) "
				+ "VALUES(?,?,?,?)";

		try {
			Connection conn = this.connect();
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, prodCode);
			pstmt.setString(2, prodName);
			pstmt.setInt(3, prodPrice);
			int val = isAdultOnly ? 1 : 0;
			pstmt.setInt(4, val);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Product getProduct(int productCode) {
		String query = "SELECT * FROM Product where ProductCode=" + productCode;

		try {
			Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			Product p = new Product(productCode, rs.getString("ProductName"), 
						1, rs.getInt("ProductPrice"), (rs.getInt("IsAdultOnly") == 1));
			return p;
		} catch (SQLException e) {
			return null;
		}
	}

	public Iterator<Product> getProductDB() {
		String query = "SELECT * FROM Product";
		List<Product> list = new LinkedList<Product>();

		try {
			Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			// loop through the result set
			while (rs.next()) {
				Product tempProduct = new Product();
				tempProduct.setProductNumber(rs.getInt("ProductCode"));
				tempProduct.setProductName(rs.getString("ProductName"));
				tempProduct.setProductPrice(rs.getInt("ProductPrice"));
				tempProduct.setIsAdultOnly(rs.getInt("IsAdultOnly") == 1);
						
						
				list.add(tempProduct);
			}
			return list.iterator();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static void createNewDatabase() {
		String url = "jdbc:sqlite:" + dbName;
		try {
			Connection conn = DriverManager.getConnection(url);
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void createNewTable() { // SQLite connection string
		String url = "jdbc:sqlite:" + dbName;

		// SQL statement for creating a new table
		String sql = "CREATE TABLE `Product` (\n" + " `ProductCode` integer UNIQUE,\n"
				+ " `ProductName` text NOT NULL,\n" + " `ProductPrice` integer NOT NULL,\n"
				+ " `IsAdultOnly` integer NOT NULL\n" + ");";

		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
}
