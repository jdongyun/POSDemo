package basicpos.model;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Cart {
	private Map<Integer, Product> cartMap; //key는 물건의 번호, value는 물건 객체
	private int productCount;
	private int cartPrice;
	
	public Cart() {
		this.cartMap = new HashMap<Integer, Product>();
		this.productCount = 0;
		this.cartPrice = 0;
	}
	/*
	 * 반환값이 참이면 물건을 새로 추가한 것임.
	 * 반환값이 거짓이면 이미 있던 물건에 물품 개수 값만 바꾼 것임.
	 */
	public boolean addProduct(Product product) {
		int tProdCode = product.getProductCode(); //temp product code의 약자
		
		if(cartMap.containsKey(tProdCode)) { //이미 물건이 추가되어 있으므로, 물건의 개수를 더한다.
			Product aProd = cartMap.get(tProdCode); 
			int newCount = aProd.getProductCount() + product.getProductCount();
			aProd.setProductCount(newCount);
			refreshPrice();
			return false;
		} else {
			cartMap.put(tProdCode, product);
			this.productCount++;
			refreshPrice();
			return true;
		}
	}
	
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
	
	public int getAllPrice() {
		return this.cartPrice;
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
	/*
	 * 삭제는 나중에 구현합니다.
	public boolean removeProduct(int prodCode, int prodCount) {
		
		if(cartMap.containsKey(prodCode)) { //Map에 객체가 있으므로, prodCount만큼 빼면 된다.
			
		}
	}
	*/
}
