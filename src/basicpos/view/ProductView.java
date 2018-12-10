package basicpos.view;

public class ProductView extends ReceiptView {
	private int prodCode;
	private int prodRemain;
	
	public ProductView() {
		this.PRINT_COUNT = 49;
	}
	
	public void printHead() {
			System.out.println("물품번호  물품명                   단가  재고수량");
	}

	public void printBody() {
			System.out.printf("  %6d  ", this.prodCode);
			
			System.out.print(this.prodName);
			for(int i = 0; i < (20 - getLength(this.prodName)); i++)
				System.out.print(" ");
			
			System.out.printf("%,9d", this.prodPrice);
			System.out.printf("  %,8d\n", this.prodRemain);
	}
	
	public void setData(int prodCode, String prodName, int prodPrice, int prodRemain) {
		this.prodCode = prodCode;
		this.prodName = prodName;
		this.prodPrice = prodPrice;
		this.prodRemain = prodRemain;
	}
	
}
