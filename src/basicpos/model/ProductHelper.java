package basicpos.model;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Collection;

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
/*
	public void selectAll() {
		String query = "SELECT * FROM Product";

		try {
			Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			Collection<Product> products = new Collection<Product>();
			// loop through the result set
			while (rs.next()) {
				System.out.printf("%5d  ", rs.getInt("ProductCode"));
				
				System.out.print(rs.getString("ProductName"));
				for(int i = 0; i < (20 - getLength(rs.getString("ProductName"))); i++)
					System.out.print(" ");
				System.out.printf("  %9d %d\n", rs.getInt("ProductPrice"), rs.getInt("IsAdultOnly"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	*/
	
	private int getLength(String str) {
		//고정폭 글꼴과 EUC-KR 문자 인코딩을 이용,
		//한글이 EUC-KR 인코딩에서 2바이트인 점을 이용해,
		//고정폭 글꼴에서 차지하는 총 문자의 너비를 계산.
		try {
			byte[] bytes = str.getBytes("ms949");
			return bytes.length;
		} catch (UnsupportedEncodingException e){
			System.out.println(e.getMessage());
			return -1;
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
