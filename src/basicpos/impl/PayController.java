package basicpos.impl;

import basicpos.model.Cart;
import basicpos.model.DBHelper;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;

public abstract class PayController {
	protected AppView appView;
	protected ProductHelper prodData;
	protected Cart cart;
	
	public PayController() {
		this.appView = new AppView();
		this.prodData = new ProductHelper();
		this.cart = new Cart();
	}
	
	public void run() {
		this.printHead();
		appView.printMessage("\n\n");
		
		this.addCart();
		appView.printMessage("\n\n");
		
		this.pay();
		appView.printMessage("\n\n");
		
		
	}

	private void addCart() {
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
			
			appView.printMessage("\n\n\n");
			
			this.printAllProduct();
			System.out.println();
		}
	}

	protected abstract void printAllProduct();
	
	protected abstract void pay();
	
	protected abstract void printHead();
}
