package basicpos.view;
import java.io.UnsupportedEncodingException;

import basicpos.Enums;

public class ReceiptView implements PrintView {
	protected final static char WALL = '-';
	protected int PRINT_COUNT = 50;
	
	protected String prodName;
	protected int prodCount;
	protected int prodPrice;
	
	private int index;
	
	public ReceiptView() {
		
	}
	
	protected int getLength(String str) {
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

	public void printHead() {
		System.out.println("번호  물품명              수량      단가      총액");
	}
	
	public void printLine() {
		//int printCount = 50; //Default print count
		for(int i = 0; i < PRINT_COUNT; i++) {
			System.out.print(WALL);
		}
		System.out.println();
		//System.out.println("------------------------------------------");
	}
	
	public void printBody() {
		if(this.prodCount == 0) {
			System.out.printf("  %6d  ", this.index);
			
			System.out.print(this.prodName);
			for(int i = 0; i < (20 - getLength(this.prodName)); i++)
				System.out.print(" ");
			
			System.out.printf("%,9d\n", this.prodPrice);
		} else {
			System.out.printf("  %2d  ", this.index);
			
			System.out.print(this.prodName);
			for(int i = 0; i < (20 - getLength(this.prodName)); i++)
				System.out.print(" ");
				
			System.out.printf("  %2d %,9d %,9d\n", this.prodCount, this.prodPrice, 
						(this.prodPrice * this.prodCount));
		}
	}
	
	public void setData(int index, String prodName, int prodCount, int prodPrice) {
		this.index = index;
		this.prodName = prodName;
		this.prodCount = prodCount;
		this.prodPrice = prodPrice;
	}
	
	public void printDiscountPrice(int discountPrice) {
		System.out.printf("할 인 액 %,41d\n", discountPrice);
	}
	
	public void printReceiptPrice(int sumPrice) {
		
		System.out.printf("결 제 액 %,41d\n", sumPrice);
	}
	
	public void printPurchageType(Enums pType) {
		String typeString = "";
		switch(pType) {
		case PURCHASE_CARD:
			typeString = "신용카드";
			break;
		case PURCHASE_CASH:
			typeString = "현금";
			break;
		default:
			break;
		}
		System.out.print("결제방식");
		for(int i = 0; i < (42 - getLength(typeString)); i++)
			System.out.print(" ");
		System.out.println(typeString);
	}
	
	public void printCashData(int receivedCash, int sumPrice) {
		System.out.printf("받은금액 %,41d\n", receivedCash);
		System.out.printf("거스름돈 %,41d\n", (receivedCash - sumPrice));
	}
	
	public void printTaxNumberForBuisness(String taxNumber)	{
		System.out.printf("현금영수증(사업자 증빙용) 번호 %19s\n", taxNumber);
	}
	public void printTaxNumberForNormal(String taxNumber)	{
		System.out.printf("현금영수증(개인용) 번호 %26s\n", taxNumber);
	}
}
