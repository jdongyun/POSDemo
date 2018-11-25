package basicpos.controller;

import java.util.Collection;
import java.util.Iterator;

import basicpos.impl.PayController;
import basicpos.model.Cart;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.ReceiptView;

public class RefundController extends PayController {
	
	@Override
	protected void pay() {
		this.printAllProduct();
		
		System.out.println("\n");

		appView.printNotice("결제 시 사용한 수단을 선택해 주세요.");
		appView.printMessage("(1) 신용카드     (2) 현금     (0) 종료");
		int purchaseType = 0;
		
		while (true) {
			purchaseType = appView.inputInt();
			if (purchaseType == 1 || purchaseType == 2) {
				break;
			}
			if (purchaseType == 0) {
				return;
			}
			appView.printError("올바른 번호가 아닙니다.");
		}
		
		if(purchaseType == 1) { //카드 번호 입력
			
			appView.printNotice("신용카드 환불입니다.");
			appView.printNotice("신용카드 번호를 입력해 주세요.");
			String cardNumber = appView.inputString();
			appView.printNotice("환불이 완료되었습니다.");
			
		} else if(purchaseType == 2) {
			
			appView.printNotice("현금 환불입니다.");
			appView.printNotice(String.format("정산할 금액은 %,d원 입니다.", cart.getAllPrice()));
			
			appView.printNotice("금액을 정산하신 후 엔터 버튼을 눌러주세요.");
			appView.inputEnter();
			appView.printNotice("환불이 완료되었습니다.");
			
		}
	}

	@Override
	protected void printHead() {
		appView.printNotice("물품 환불 기능입니다.");
	}
	
	@Override
	protected void printAllProduct() {
		Collection<Product> products = this.cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		int index = 1;
		ReceiptView receiptView = new ReceiptView();

		receiptView.printReceiptLine();
		receiptView.printReceiptHead();
		receiptView.printReceiptLine();
		while (ite.hasNext()) {
			Product tempProduct = ite.next();
			receiptView.printReceiptProduct(index, tempProduct.getProductName(), tempProduct.getProductCount(),
					tempProduct.getProductPrice());
			index++;
		}
		receiptView.printReceiptLine();
		receiptView.printReceiptPrice(this.cart.getAllPrice());
		receiptView.printReceiptLine();
	}
}
