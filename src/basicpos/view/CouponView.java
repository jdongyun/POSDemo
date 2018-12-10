package basicpos.view;

public class CouponView implements PrintView {
	private final static char WALL = '-';
	private int couponCode;
	private int productCode;
	private int productCount;
	private int discountRate;
	private boolean isUsed;
	
	public CouponView() {
		
	}
	
	public void printHead() {
		System.out.println("쿠폰 코드      물품 코드   개수  할인비율  사용여부");
	}
	
	public void printLine() {
		int printCount = 51; //Default print count
		for(int i = 0; i < printCount; i++) {
			System.out.print(WALL);
		}
		System.out.println();
	}
	
	public void printBody() {
		System.out.println(String.format("%9d     %10d   %4d      %4d  %s", 
				couponCode, productCode, productCount, discountRate, isUsed));
	}
	
	public void setBody(int couponCode, int productCode, int productCount, int discountRate, boolean isUsed) {
		this.couponCode = couponCode;
		this.productCode = productCode;
		this.productCount = productCount;
		this.discountRate = discountRate;
		this.isUsed = isUsed;
	}
}
