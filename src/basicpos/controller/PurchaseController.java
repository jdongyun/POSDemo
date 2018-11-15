package basicpos.controller;

import java.util.Collection;
import java.util.Iterator;

import basicpos.model.Cart;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.ReceiptView;

public class PurchaseController {
	private AppView appView;
	private ProductHelper prodData;
	
	public PurchaseController() {
		this.appView = new AppView();
		this.prodData = new ProductHelper();
	}

	public void purchase() {
		Cart cart = new Cart();
		boolean isAdultOnly = false;
		
		appView.printNotice("물건 계산 기능입니다.\n\n");
		while(true) {
			Product product;
			
			appView.printNotice("물건의 코드 번호를 입력해 주세요. (종료는 0 입력)");
			
			int input = appView.inputInt();
			
			if(input == 0) break;
			product = prodData.getProduct(input);
			if(product == null) {
				appView.printError("올바른 코드 번호가 아닙니다.");
				continue;
			}
			
			appView.printNotice("물건의 개수를 입력해 주세요. (종료는 0 입력)");
			
			String count = appView.inputString();
			
			if(count.equals("0")) break;
			if(!count.equals("")) {
				product.setProductCount(Integer.parseInt(count));
			}

			cart.addProduct(product);
			
			if(product.getIsAdultOnly()) isAdultOnly = true;
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
			this.printAllProduct(cart);
			System.out.println();
		}
		
		//바코드 다 찍음
		
		System.out.println("\n\n\n\n");
		this.printAllProduct(cart);
		System.out.println("\n");
		if(isAdultOnly) {
			appView.printNotice("나이를 확인해야 하는 물품이 장바구니에 있습니다.\n");
		}
		appView.printNotice("결제 방식을 선택해 주세요.");
		appView.printMessage("(1) 신용카드     (2) 현금     (0) 종료");
		int purchaseType = 0;
		while(true) {
			purchaseType = appView.inputInt();
			if(purchaseType == 1 || purchaseType == 2) {
				break;
			}
			if(purchaseType == 0) {
				return;
			}
			appView.printError("올바른 번호가 아닙니다.");
		}
		
		int receivedCash = cart.getAllPrice(); //카드결제일 때의 기본값은 결제금액.
		
		if(purchaseType == 2) {
			appView.printNotice("현금 결제입니다.");
			appView.printNotice(String.format("판매액은 %,d원입니다. 받으신 금액을 입력해 주세요.\n", 
												cart.getAllPrice()));
			receivedCash = appView.inputInt();
			appView.printNotice(String.format("받으신 금액은 %,d원이며, 거스름돈은 %,d원입니다.\n", 
								receivedCash, (receivedCash - cart.getAllPrice())));
		}
		
		System.out.println("\n\n");
		
		this.printReceipt(cart, purchaseType, receivedCash);
		
		System.out.println("\n\n");
	}
	
	private void printAllProduct(Cart cart) {
		Collection<Product> products = cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		int index = 1;
		ReceiptView receiptView = new ReceiptView();
		
		receiptView.printReceiptLine();
		receiptView.printReceiptHead();
		receiptView.printReceiptLine();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			receiptView.printReceiptProduct(index, tempProduct.getProductName(), 
					tempProduct.getProductCount(), tempProduct.getProductPrice());
			index++;
		}
		receiptView.printReceiptLine();
		receiptView.printReceiptPrice(cart.getAllPrice());
		receiptView.printReceiptLine();
	}
	
	private void printReceipt(Cart cart, int purchaseType, int receivedCash) {
		Collection<Product> products = cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		int index = 1;
		ReceiptView receiptView = new ReceiptView();
		
		receiptView.printReceiptLine();
		receiptView.printReceiptHead();
		receiptView.printReceiptLine();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			receiptView.printReceiptProduct(index, tempProduct.getProductName(), 
					tempProduct.getProductCount(), tempProduct.getProductPrice());
			index++;
		}
		receiptView.printReceiptLine();
		receiptView.printReceiptPrice(cart.getAllPrice());
		if(purchaseType == 1) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CARD);
		} else if(purchaseType == 2) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CASH);
		}
		
		receiptView.printReceiptLine();
		receiptView.printCashData(receivedCash, cart.getAllPrice());
		receiptView.printReceiptLine();
		
		System.out.println();
		
	}
}
