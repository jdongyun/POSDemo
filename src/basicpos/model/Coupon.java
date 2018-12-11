package basicpos.model;

public class Coupon {
	private int couponCode;
	private int productCode;
	private int productCount;
	private int discountRate;
	private boolean isUsed;
	
	public Coupon(int couponCode, int productCode, int productCount, int discountRate, boolean isUsed) {
		this.couponCode = couponCode;
		this.productCode = productCode;
		this.productCount = productCount;
		this.discountRate = discountRate;
		this.isUsed = isUsed;
	}
	
	public int getCouponCode() {
		return this.couponCode;
	}
	
	public int getProductCode() {
		return this.productCode;
	}
	
	public int getProductCount() {
		return this.productCount;
	}
	
	public int getDiscountRate() {
		return this.discountRate;
	}
	
	public boolean getIsUsed() {
		return this.isUsed;
	}
	
	public void setIsUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
}