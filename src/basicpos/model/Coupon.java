package basicpos.model;

public class Coupon {
	private int couponCode;
	private int productCode;
	
	public Coupon(int couponCode, int productCode) {
		this.couponCode = couponCode;
		this.productCode = productCode;
	}
	
	public int getCouponCode() {
		return this.couponCode;
	}
	
	public int getProductCode() {
		return this.productCode;
	}
	
	public String toString() {
		return String.format("%16d     %d", couponCode, productCode);
	}
}