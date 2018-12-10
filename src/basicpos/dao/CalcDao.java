package basicpos.dao;

import java.util.Collection;
import java.util.Iterator;

import basicpos.Enums;
import basicpos.model.Cart;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.PrintLineView;

public abstract class CalcDao {
	protected AppView appView;
	protected Cart cart;
	protected PrintLineView plView;
	
	private Enums CALC_TYPE;
	private final static int PRODUCT_MIN_REMAIN = 5;
	private final static int PRODUCT_ORDER_QUANTITY = 30;
	
	public CalcDao(Enums type) {
		this.appView = new AppView();
		this.cart = new Cart();
		this.CALC_TYPE = type;
	}
	
	public void run() {
		this.printHead();
		appView.printMessage("\n\n");
		
		this.addCart();
		appView.printMessage("\n\n");
		
		this.pay();
		appView.printMessage("\n\n");
		
		//재고수량 수정
		updateProductRemain();
		
	}

	private void addCart() {
		while (true) {
			Product product;

			appView.printNotice("물건의 코드 번호를 입력해 주세요. (종료는 0 입력)");

			int input = appView.inputInt();

			if (input == 0)
				return;

			product = ProductHelper.getProduct(input);

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
			if(product.getProductRemain() < count) {
				product = null;
				appView.printNotice("재고수량보다 많이 물건을 구매할 수 없습니다.");
				continue;
			}

			product.setProductCount(count);

			if (product != null)
				this.cart.addProduct(product);
			
			appView.printMessage("\n\n\n");
			
			this.printAllProduct();
			System.out.println();
		}
	}
	
	protected void updateProductRemain() {
		Collection<Product> products = this.cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		
		while (ite.hasNext()) {
			Product tempProduct = ite.next();
			if(CALC_TYPE == Enums.TYPE_PURCHASE) { //결제
				tempProduct.setProductRemain(tempProduct.getProductRemain() - tempProduct.getProductCount());
			} else if(CALC_TYPE == Enums.TYPE_REFUND) { //환불
				tempProduct.setProductRemain(tempProduct.getProductRemain() + tempProduct.getProductCount());
			}
			
			if(tempProduct.getProductRemain() < PRODUCT_MIN_REMAIN) {
				appView.printNotice(tempProduct.getProductName() + " 물품의 재고수량이 부족하여 자동으로 주문처리 하였습니다.");
				appView.printNotice("물품의 자동 구매 수량은 " + PRODUCT_ORDER_QUANTITY + "개 입니다.");
				tempProduct.setProductRemain(tempProduct.getProductRemain() + PRODUCT_ORDER_QUANTITY);
			}
			ProductHelper.updateProduct(tempProduct);
		}
	}

	protected abstract void printAllProduct();
	
	protected abstract void pay();
	
	protected abstract void printHead();
}
