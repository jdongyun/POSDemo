package basicpos.controller;

import java.util.Collection;
import java.util.Iterator;

import basicpos.impl.PayController;
import basicpos.model.Cart;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.ReceiptView;

public class PurchaseController extends PayController {
	
	
	@Override
	protected void pay() {

		this.printAllProduct();

		System.out.println("\n");

		if (cart.hasAdultProduct()) {
			appView.printNotice("나이를 확인해야 하는 물품이 있습니다.\n");
		}

		appView.printNotice("결제 수단을 선택해 주세요.");
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

		int receivedCash = this.cart.getAllPrice(); // 카드결제일 때의 기본값은 결제금액.
		String taxNumber = null;

		if(purchaseType == 1) {
			
			appView.printNotice("신용카드 결제입니다.");
			appView.printNotice("신용카드 번호를 입력해 주세요.");
			String cardNumber = appView.inputString();
			appView.printNotice("결제가 완료되었습니다.");
			
		} else if (purchaseType == 2) {
			
			appView.printNotice("현금 결제입니다.");
			appView.printNotice(String.format("판매액은 %,d원입니다. 받으신 금액을 입력해 주세요.\n", cart.getAllPrice()));
			receivedCash = appView.inputInt();
			
			appView.printNotice(String.format("받으신 금액은 %,d원이며, 거스름돈은 %,d원입니다.\n", receivedCash,
					(receivedCash - cart.getAllPrice())));
			
			appView.printNotice("금액을 정산하신 후 엔터 버튼을 눌러주세요.");
			appView.inputEnter();
			
			appView.printNotice("현금영수증 여부를 선택해 주세요.");
			appView.printMessage("(1) 사업자 증빙용   (2) 개인용     (0) 건너뛰기");
			int receiptType = 0;
			boolean isComplete = false;
			while (!isComplete) {
				receiptType = appView.inputInt();
				
				switch(receiptType) {
				case 1:
					appView.printNotice("사업자 번호를 입력하세요.");
					taxNumber = appView.inputString();
					while (taxNumber.length() != 10) {
						appView.printError("올바른 번호가 아닙니다.");
						taxNumber = appView.inputString();
					}
					isComplete = true;
					break;
				case 2:
					// 개인 휴대전화 번호 입력 받음(11자리)
					appView.printNotice("휴대전화 번호를 입력하세요.");
					taxNumber = appView.inputString();
					while (taxNumber.length() != 11) {
						appView.printError("올바른 번호가 아닙니다.");
						taxNumber = appView.inputString();
					}
					isComplete = true;
					break;
				case 0:
					isComplete = true;
					break;
				default:
					//Nothing. goto while loop
				}
				
			}
		}

		System.out.println("\n\n");

		this.printReceipt(purchaseType, receivedCash, taxNumber);

	}

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

	private void printReceipt(int purchaseType, int receivedCash, String taxNumber) {
		Collection<Product> products = this.cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		int index = 1;
		ReceiptView receiptView = new ReceiptView();

		receiptView.printReceiptLine();
		receiptView.printReceiptHead();
		receiptView.printReceiptLine();
		
		//계산 목록 시작
		while (ite.hasNext()) {
			Product tempProduct = ite.next();
			receiptView.printReceiptProduct(index, tempProduct.getProductName(), tempProduct.getProductCount(),
					tempProduct.getProductPrice());
		}
		//계산 목록 끝
		
		//판매액 및 결제방식 시작
		receiptView.printReceiptLine();
		receiptView.printReceiptPrice(this.cart.getAllPrice());
		if (purchaseType == 1) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CARD);
		} else if (purchaseType == 2) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CASH);
		}
		//판매액 및 결제방식 끝
		
		//받은금액 시작
		receiptView.printReceiptLine();
		receiptView.printCashData(receivedCash, this.cart.getAllPrice());
		receiptView.printReceiptLine();
		//받은금액 끝
		
		//현금영수증 시작
		if(taxNumber != null) {
			if (taxNumber.length() == 10) { // 사업자 증빙용
				receiptView.printTaxNumberForBuisness(taxNumber);
				receiptView.printReceiptLine();
			} else if (taxNumber.length() == 11) { // 개인용
				receiptView.printTaxNumberForNormal(taxNumber);
				receiptView.printReceiptLine();
			}
		}
		//현금영수증 끝

		System.out.println();
	}
	

	@Override
	protected void printHead() {
		appView.printNotice("물품 계산 기능입니다.");
	}
}
