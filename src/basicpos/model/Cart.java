package basicpos.model;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Cart {
	private Map<Integer, Product> cartMap; //key는 물건의 번호, value는 물건 객체
	private int cartPrice;
	private int discountPrice;
	
	public Cart() {
		this.cartMap = new HashMap<Integer, Product>();
		this.cartPrice = 0;
		this.discountPrice = 0;
	}
	/*
	 * 반환값이 참이면 물건을 새로 추가한 것이고
	 * 반환값이 거짓이면 이미 있던 물건에 물품 개수 값만 바꾼 것.
	 */
	public boolean addProduct(Product product) {
		int tProdCode = product.getProductCode(); //물품 코드
		
		if(cartMap.containsKey(tProdCode)) { //이미 물건이 추가되어 있으므로, 물건의 개수를 더한다.
			Product aProd = cartMap.get(tProdCode); 
			int newCount = aProd.getProductCount() + product.getProductCount();
			aProd.setProductCount(newCount);
			refreshPrice();
			return false;
		} else {
			cartMap.put(tProdCode, product);
			refreshPrice();
			return true;
		}
	}
	
	//카트에 새로운 물품이 추가되면 총 금액을 갱신해야 함
	public void refreshPrice() {
		this.cartPrice = 0;
		Collection<Product> collection = cartMap.values();
		Iterator<Product> ite = collection.iterator();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			int tempPrice = tempProduct.getProductPrice() * tempProduct.getProductCount();
			this.cartPrice += tempPrice;
		}
	}
	
	public int getCartPrice() {
		return this.cartPrice;
	}
	
	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}
	
	public int getDiscountPrice() {
		return this.discountPrice;
	}
	
	public int getFinalPrice() {
		return this.cartPrice - this.discountPrice;
	}
	
	public Collection<Product> getAllProduct() {
		Collection<Product> collection = cartMap.values();
		return collection;
	}
	
	public boolean hasAdultProduct() {
		Collection<Product> collection = cartMap.values();
		Iterator<Product> ite = collection.iterator();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			if(tempProduct.getIsAdultOnly()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEmpty() {
		return (this.cartPrice == 0);
	}
}
