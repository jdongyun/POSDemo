package basicpos.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	/*
	public String toString() {
		return String.format("%16d     %16d   %2d   %3d   %d", 
				couponCode, productCode, productCount, discountRate, isUsed ? 1 : 0);
	}*/
}