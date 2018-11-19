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
	private Cart cart;

	public PurchaseController() {
		this.appView = new AppView();
		this.prodData = new ProductHelper();
		this.cart = new Cart();
	}

	public void purchase() {
		appView.printNotice("물건 계산 기능입니다.\n\n");
		while (true) {
			Product product;

			appView.printNotice("물건의 코드 번호를 입력해 주세요. (종료는 0 입력)");

			int input = appView.inputInt();

			if (input == 0)
				break;

			product = prodData.getProduct(input);

			if (product == null) {
				appView.printError("올바른 코드 번호가 아닙니다.");
				continue;
			}

			appView.printNotice("물건의 개수를 입력해 주세요. (종료는 0 입력)");

			int count = appView.inputInt();

			if (count == 0) {
				product = null;
				appView.printNotice("물건을 추가하지 않았습니다.");
				break;
			}

			product.setProductCount(count);

			if (product != null)
				this.cart.addProduct(product);

			System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
			this.printAllProduct();
			System.out.println();
		}

		// 바코드 다 찍음

		System.out.println("\n\n\n\n");

		this.printAllProduct();

		System.out.println("\n");

		if (cart.hasAdultProduct()) {
			appView.printNotice("나이를 확인해야 하는 물품이 있습니다.\n");
		}

		appView.printNotice("결제 방식을 선택해 주세요.");
		appView.printMessage("(1) 신용카드     (2) 현금     (0) 종료");
		int purchaseType = 0;
		String number = null;
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

		if (purchaseType == 2) {
			appView.printNotice("현금 결제입니다.");
			appView.printNotice(String.format("판매액은 %,d원입니다. 받으신 금액을 입력해 주세요.\n", cart.getAllPrice()));
			receivedCash = appView.inputInt();
			appView.printNotice(String.format("받으신 금액은 %,d원이며, 거스름돈은 %,d원입니다.\n", receivedCash,
					(receivedCash - cart.getAllPrice())));
			appView.printNotice("금액을 정산하신 후 엔터 버튼을 눌러주세요.");
			appView.inputEnter();
			appView.printNotice("현금영수증 여부를 선택해 주세요.");
			appView.printMessage("(1) 사업자 증빙용   (2) 개인용     (0) 하지 않음");
			int receiptType = 0;
			while (true) {
				receiptType = appView.inputInt();
				if (receiptType == 1) {
					// 사업자 번호 입력 받음(10자리)
					System.out.println("사업자 번호를 입력하세요.");
					number = appView.inputString();
					while (number.length() != 10) {
						appView.printError("올바른 번호가 아닙니다.");
						number = appView.inputString();
					}
					break;
				} else if (receiptType == 2) {
					// 개인 핸드폰번호 입력 받음(11자리)
					System.out.println("핸드폰 번호를 입력하세요.");
					number = appView.inputString();
					while (number.length() != 11) {
						appView.printError("올바른 번호가 아닙니다.");
						number = appView.inputString();
					}
					break;
				}
				if (receiptType == 0) {
					break;
				}
			}
			appView.printNotice("영수증이 출력됩니다.");
		}

		System.out.println("\n\n");

		this.printReceipt(purchaseType, receivedCash, number);

		System.out.println("\n\n");
	}

	private void printAllProduct() {
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

	private void printReceipt(int purchaseType, int receivedCash, String CashReciept) {
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
		}
		receiptView.printReceiptLine();
		receiptView.printReceiptPrice(this.cart.getAllPrice());
		if (purchaseType == 1) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CARD);
		} else if (purchaseType == 2) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CASH);
		}

		receiptView.printReceiptLine();
		receiptView.printCashData(receivedCash, this.cart.getAllPrice());
		receiptView.printReceiptLine();
		if (CashReciept.length() == 10) {// 사업자 증빙용
			receiptView.printReceiptLine();
			receiptView.printCashRecieptForBuisness(CashReciept);
			receiptView.printReceiptLine();
		} else if (CashReciept.length() == 11) {// 개인
			receiptView.printReceiptLine();
			receiptView.printCashRecieptForNormal(CashReciept);
			receiptView.printReceiptLine();
		}

		System.out.println();

	}
}
