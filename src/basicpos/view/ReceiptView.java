package basicpos.view;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class ReceiptView {
	private final static char WALL = '-';
	public static enum PurchaseType {
		PURCHASE_CARD,
		PURCHASE_CASH
	}
	
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

	public void printReceiptHead() {
		System.out.println("번호  물품명              수량      단가      총액");
	}
	
	public void printReceiptLine() {
		for(int i = 0; i < 50; i++) {
			System.out.print(WALL);
		}
		System.out.println();
		//System.out.println("------------------------------------------");
	}
	
	public void printReceiptProduct(int index, String prodName, int prodCount, int prodPrice) {

		System.out.printf("  %2d  ", index);
		
		System.out.print(prodName);
		for(int i = 0; i < (20 - getLength(prodName)); i++)
			System.out.print(" ");
		System.out.printf("  %2d %,9d %,9d\n", prodCount, prodPrice, (prodPrice * prodCount));
	}
	
	public void printReceiptPrice(int sumPrice) {
		System.out.printf("판매액   %,41d\n", sumPrice);
	}
	
	public void printPurchageType(PurchaseType pType) {
		String typeString = "";
		switch(pType) {
		case PURCHASE_CARD:
			typeString = "신용카드";
			break;
		case PURCHASE_CASH:
			typeString = "현금";
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
}
