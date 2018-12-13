package basicpos.controller;

import java.io.File;
import java.util.Iterator;

import basicpos.model.Coupon;
import basicpos.model.CouponHelper;
import basicpos.model.DBHelper;
import basicpos.model.Point;
import basicpos.model.PointHelper;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.CouponView;
import basicpos.view.MainView;
import basicpos.view.PointView;
import basicpos.view.PrintLineView;
import basicpos.view.ProductView;
import basicpos.view.ReceiptView;

public class ManageController {
	private AppView appView;
	private MainView mainView;
	
	public ManageController() {
		this.appView = new AppView();
		this.mainView = new MainView();
	}
	
	public void run() {
		//관리자 페이지에서의 리스트
		mainView.addView("1. 물품 목록");
		mainView.addView("2. 물품 추가");
		mainView.addView("3. 물품 수정");
		mainView.addView("4. 물품 제거");
		
		mainView.addView("5. 포인트 정보");
		mainView.addView("6. 포인트 정보 수정");
		
		mainView.addView("7. 쿠폰 정보");
		mainView.addView("8. 쿠폰 추가");
		mainView.addView("9. 쿠폰 제거");
		
		mainView.addView("10. DB 초기화");
		
		mainView.addView("0. 뒤로 가기");
		
		
		appView.printNotice("관리자 페이지입니다.");
		
		while(true) {
			mainView.printList();
			int input = appView.inputInt();
			
			switch(input) {
			case 1: //물품 목록 출력
				appView.printNotice("물품 목록을 출력합니다.");
				this.printProduct();
				break;
			case 2: //물품 추가
				appView.printNotice("물품을 추가합니다.");
				this.insertProduct();
				break;
			case 3: //물품 수정
				appView.printNotice("물품을 수정합니다.");
				this.updateProduct();
				break;
			case 4: //물품 제거
				appView.printNotice("물품을 제거합니다.");
				this.deleteProduct();
				break;
			case 5: //포인트 정보 출력
				appView.printNotice("고객들의 포인트 정보를 출력합니다.");
				this.printPointData();
				break;
			case 6: //포인트 정보 수정
				this.updatePoint();
				break;
			case 7: //쿠폰 정보
				appView.printNotice("쿠폰 정보를 출력합니다.");
				this.printCouponData();
				break;
			case 8: //쿠폰 추가
				appView.printNotice("쿠폰을 추가합니다.");
				this.insertCoupon();
				break;
			case 9: //쿠폰 삭제
				appView.printNotice("쿠폰을 제거합니다.");
				this.deleteCoupon();
				break;
			case 10: //데이터베이스 초기화
				appView.printNotice("데이터베이스를 모두 초기화합니다.");
				appView.printNotice("동의는 y를, 거부는 다른 키를 입력해 주세요.");
				String inputString = appView.inputString();
				if(inputString.equals("y")) {
					File dbFile = new File(DBHelper.dbName);
					if(!dbFile.exists() || (dbFile.exists() && dbFile.delete())) {
						DBHelper.createNewDatabase();
						ProductHelper.createNewTable();
						CouponHelper.createNewTable();
						PointHelper.createNewTable();
						appView.printNotice("데이터베이스 초기화가 완료되었습니다.");
					} else {
						appView.printError("데이터베이스 초기화 중 문제가 발생하였습니다.");
					}
				}
				break;
			case 0:
				appView.printNotice("메인 메뉴로 이동합니다.");
				return;
			default:
				appView.printError("다시 입력해 주십시오.");
			}
		}
	}
	
	//물품 목록 출력
	private void printProduct() {
		ProductView productView = new ProductView();
		Iterator<Product> ite = ProductHelper.getProductList();
		
		appView.printMessage("");
		productView.printLine();
		productView.printHead();
		productView.printLine();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			
			productView.setData(tempProduct.getProductCode(), 
					tempProduct.getProductName(), 
					tempProduct.getProductPrice(),
					tempProduct.getProductRemain());
			productView.printBody();
		}
		productView.printLine();
		appView.printMessage("");
	}
	
	//고객들의 포인트 정보 출력
	private void printPointData() {
		PointView pointView = new PointView();
		Iterator<Point> ite = PointHelper.getPointDB();

		pointView.printLine();
		pointView.printHead();
		pointView.printLine();
		
		while(ite.hasNext()) {
			Point tempPoint = ite.next();
			pointView.setBody(tempPoint.getUserCode(), tempPoint.getUserPoint());
			pointView.printBody();
		}
		
		pointView.printLine();
	}
	
	//포인트 정보 업데이트
	private void updatePoint() {
		this.printPointData();
		appView.printNotice("고객 포인트 번호를 입력해 주세요. (없는 포인트 번호 입력 시 추가를 수행합니다. 취소는 0 입력)");
		int input = appView.inputInt();
		if(input == 0) return;
		
		Point point = PointHelper.getPoint(input);
		if(point == null) { //해당하는 고객 포인트 번호가 없으므로 추가하기
			appView.printError("해당하는 고객 포인트 번호가 없으므로 추가를 수행합니다.");
			point = new Point(input, 0);
			
			appView.printNotice("입력할 포인트 잔액을 입력해 주세요.");
			int balance = appView.inputInt();
			if(balance < 0) {
				appView.printNotice("음수를 입력할 수 없습니다.");
				return;
			}
			point.setUserPoint(balance);
			if(PointHelper.insertPoint(point)) {
				appView.printNotice("포인트 번호가 추가되었습니다.");
			} else {
				appView.printNotice("포인트 번호 추가 중 문제가 발생하였습니다.");
			}
			
			return;
		} else { //해당하는 고객 포인트 번호의 값을 수정
			appView.printNotice(String.format("현재 포인트 잔액은 %,d원입니다.", point.getUserPoint()));
			appView.printNotice("수정할 포인트 잔액을 입력해 주세요.");
			int balance = appView.inputInt();
			if(balance < 0) {
				appView.printNotice("음수를 입력할 수 없습니다.");
				return;
			}
			point.setUserPoint(balance);
			if(PointHelper.updatePoint(point)) {
				appView.printNotice("포인트 잔액이 수정되었습니다.");
			} else {
				appView.printNotice("포인트 잔액 수정 중 문제가 발생하였습니다.");
			}
		}
		
		
	}
	
	//쿠폰 정보 출력
	private void printCouponData() {
		CouponView couponView = new CouponView();
		Iterator<Coupon> ite = CouponHelper.getCouponDB();

		couponView.printLine();
		couponView.printHead();
		couponView.printLine();
		
		while(ite.hasNext()) {
			Coupon coupon = ite.next();
			couponView.setBody(coupon.getCouponCode(), coupon.getProductCode(), 
					coupon.getProductCount(), coupon.getDiscountRate(), coupon.getIsUsed());
			couponView.printBody();
		}
		
		couponView.printLine();
	}
	
	//물품 추가
	private void insertProduct() {
		int productCode = 0;
		String productName = "";
		int productPrice = 0;
		boolean isAdultOnly = false;
		int productRemain = 0;
		while(true) {
			appView.printNotice("추가할 물품의 코드를 입력해 주세요.");
			productCode = appView.inputInt();
			if(ProductHelper.getProduct(productCode) != null) {
				appView.printError("해당하는 물품의 코드가 이미 있습니다. 다른 코드를 입력해 주세요.");
			} else {
				break;
			}
		}

		appView.printNotice("추가할 물품의 이름을 입력해 주세요.");
		productName = appView.inputString();
		
		appView.printNotice("추가할 물품의 가격을 입력해 주세요.");
		productPrice = appView.inputInt();
		
		while(true) {
			appView.printNotice("추가할 물품이 연령 확인이 필요한지 입력해 주세요. (y/n)");
			String temp = appView.inputString();
			if(temp.equals("y")) {
				isAdultOnly = true;
				break;
			} else if(temp.equals("n")) {
				break;
			}
			else {
				appView.printError("다시 입력해 주세요.");
			}
		}
		
		appView.printNotice("추가할 물품의 재고 개수를 입력해 주세요.");
		productRemain = appView.inputInt();
		
		Product product = new Product();
		product.setProductCode(productCode);
		product.setProductName(productName);
		product.setProductPrice(productPrice);
		product.setIsAdultOnly(isAdultOnly);
		product.setProductRemain(productRemain);
		
		if(ProductHelper.insertProduct(product)) {
			appView.printNotice("물품 추가가 완료되었습니다.");
		} else {
			appView.printError("물품 추가 중 문제가 발생하였습니다.");
		}
	}
	
	//물품 정보 업데이트
	private void updateProduct() {
		this.printProduct();
		appView.printNotice("수정할 물품의 코드를 입력해 주세요.");
		int productCode = appView.inputInt();
		Product product = ProductHelper.getProduct(productCode);
		
		if(product != null) {
			appView.printNotice("수정할 부분을 선택해 주세요.");
			
			PrintLineView plView = new PrintLineView();
			plView.addView("(1) 물품명");
			plView.addView("(2) 단가");
			plView.addView("(3) 재고수량");
			plView.addView("(0) 취소");
			plView.printList();
			
			int input = appView.inputInt();
			int tempInt;
			String tempString;
			switch(input) {
			case 1:
				appView.printNotice("수정할 물품명을 입력해 주세요.");
				tempString = appView.inputString();
				product.setProductName(tempString);
				ProductHelper.updateProduct(product);
				appView.printNotice("수정이 완료되었습니다.");
				break;
				
			case 2:
				appView.printNotice("수정할 물품의 단가를 입력해 주세요.");
				tempInt = appView.inputInt();
				product.setProductPrice(tempInt);
				ProductHelper.updateProduct(product);
				appView.printNotice("수정이 완료되었습니다.");
				break;

			case 3:
				appView.printNotice("수정할 물품의 재고 수량을 입력해 주세요.");
				tempInt = appView.inputInt();
				product.setProductRemain(tempInt);
				ProductHelper.updateProduct(product);
				appView.printNotice("수정이 완료되었습니다.");
				break;

			case 0:
				appView.printNotice("물품 수정을 취소합니다.");
				break;
			default:
				appView.printError("입력이 잘못되었습니다.");
			}
		} else {
			appView.printError("해당하는 코드의 물품이 없습니다.");
		}
	}
	
	//물품 삭제
	private void deleteProduct() {
		this.printProduct();
		appView.printNotice("삭제할 물품의 코드를 입력해 주세요.");
		int productCode = appView.inputInt();
		if(ProductHelper.deleteProduct(productCode)) {
			appView.printNotice("물품 제거가 완료되었습니다.");
		} else {
			appView.printError("물품 제거 중 문제가 발생하였습니다.");
		}
	}

	//쿠폰 추가
	private void insertCoupon() {
		int couponCode = 0;
		int productCode = 0;
		int productCount = 0;
		int discountRate = 0;

		appView.printNotice("추가할 쿠폰의 코드를 입력해 주세요. (물품 코드와 중복 불가)");
		while(true) {
			couponCode = appView.inputInt();
			if(ProductHelper.getProduct(couponCode) != null) {
				appView.printError("동일한 코드로 된 물품이 이미 있습니다. 다시 입력해 주세요.");
			} else {
				break;
			}
		}
		
		appView.printNotice("추가할 쿠폰의 물품 코드를 입력해 주세요.");
		productCode = appView.inputInt();
		
		appView.printNotice("추가할 쿠폰이 적용될 물품의 개수를 입력해 주세요.");
		productCount = appView.inputInt();
		
		appView.printNotice("추가할 쿠폰의 할인 비율을 입력해 주세요. (1부터 100까지의 정수, 100은 전액 할인)");
		while(true) {
			discountRate = appView.inputInt();
			if(discountRate < 1 || discountRate > 100) {
				appView.printError("할인 비율을 다시 입력해 주세요.");
			} else {
				break;
			}
		}
		
		Coupon coupon = new Coupon(couponCode, productCode, productCount, discountRate, false);
		
		if(CouponHelper.insertCoupon(coupon)) {
			appView.printNotice("쿠폰 추가가 완료되었습니다.");
		} else {
			appView.printError("쿠폰 추가 중 문제가 발생하였습니다.");
		}
	}
	
	//쿠폰 삭제
	private void deleteCoupon() {
		this.printCouponData();
		appView.printNotice("삭제할 쿠폰의 코드를 입력해 주세요.");
		int couponCode = appView.inputInt();
		if(CouponHelper.deleteCoupon(couponCode)) {
			appView.printNotice("물품 제거가 완료되었습니다.");
		} else {
			appView.printError("물품 제거 중 문제가 발생하였습니다.");
		}
	}
}
