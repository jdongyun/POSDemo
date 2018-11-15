package basicpos.model;

public class Product {
	/*
	 * TODO:
	 * - 물품 번호
	 * - 물품 이름
	 * - 물품 개수
	 * - 물품 가격
	 * - 청소년 판매 가능 여부(술담배 여부 체크) Y는 술담배, N은 아님
	 */
	private int productCode;
	private String productName;
	private int productCount;
	private int productPrice;
	private boolean isAdultOnly;
	
	public Product(int prodCode, String prodName, int prodCount, int prodPrice, boolean isAdult) {
		this.productCode = prodCode;
		this.productName = prodName;
		this.productCount = prodCount;
		this.productPrice = prodPrice;
		this.isAdultOnly = isAdult;
	}
	
	public void setProductNumber(int prodCode) {
		this.productCode = prodCode;
	}
	
	public void setProductName(String prodName) {
		this.productName = prodName;
	}
	
	public void setProductCount(int prodCount) {
		this.productCount = prodCount;
	}
	
	public void setProductPrice(int prodPrice) {
		this.productPrice = prodPrice;
	}
	
	public void setIsAdultOnly(boolean isAdult) {
		this.isAdultOnly = isAdult;
	}
	
	public int getProductCode() {
		return this.productCode;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public int getProductCount() {
		return this.productCount;
	}
	
	public int getProductPrice() {
		return this.productPrice;
	}
	
	public boolean getIsAdultOnly() {
		return this.isAdultOnly;
	}
	
	public String getStringData() {
		return "물품명 : " + this.productName
				+ ", 개당 가격 : " + this.productPrice
				+ "원, 개수 : " + this.productCount;
	}
	
	public String toString() {
		return this.productCode + ", " + this.productName + ", " + this.productCount 
				+ ", " + this.productPrice + ", " + this.isAdultOnly;
	}
	
}
