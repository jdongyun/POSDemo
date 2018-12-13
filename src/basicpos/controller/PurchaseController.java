package basicpos.controller;

import java.util.Collection;
import java.util.Iterator;

import basicpos.Enums;
import basicpos.dao.CalcDao;
import basicpos.model.Coupon;
import basicpos.model.CouponHelper;
import basicpos.model.Point;
import basicpos.model.PointHelper;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.PrintLineView;
import basicpos.view.ReceiptView;

public class PurchaseController extends CalcDao {
	
	private final static double POINT_RATE = 0.01;
	
	public PurchaseController() {
		this(Enums.TYPE_PURCHASE);
	}
	
	private PurchaseController(Enums type) {
		super(type);
	}
	
	@Override
	protected void pay() {

		if(this.cart.isEmpty()) { //카트에 물품이 없으면 계산할 필요가 없으므로 return
			return;
		}
		this.printAllProduct(); //결제 전 카트에 들어있는 모든 물품 출력

		appView.printMessage("");

		if (cart.hasAdultProduct()) {
			appView.printNotice("나이를 확인해야 하는 물품이 있습니다.\n");
		}
		
		int purchaseType = 0; //결제 수단 저장
		int receivedCash = this.cart.getFinalPrice(); // 카드결제일 때의 기본값은 결제금액.
		String taxNumber = null; //현금영수증 번호 입력. 기본값은 null

		while(true) {
			if(this.cart.getFinalPrice() == 0) break; //쿠폰으로만 계산할 경우, 결제할 금액이 0원이므로 계산하지 않아도 됨
			
			appView.printNotice("결제 수단을 선택해 주세요.");
			
			plView = new PrintLineView();
			plView.addView("(1) 신용카드");
			plView.addView("(2) 현금");
			plView.addView("(0) 종료");
			plView.printList();
			
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
			
			if(purchaseType == 1) {
				appView.printNotice("신용카드 결제입니다.");
				
				int ret = usePoint();
				if(ret == -1) //usePoint에서 -1이 입력되면 뒤로가기로 체크
					continue;
				
				appView.printNotice("신용카드 번호를 입력해 주세요. (뒤로 가기는 0 입력)");
				String cardNumber = appView.inputString();
				if(cardNumber.equals("0")) continue;
				
				appView.printNotice("결제가 완료되었습니다.");
				
			} else if (purchaseType == 2) {
				appView.printNotice("현금 결제입니다.");

				int ret = usePoint();
				if (ret == -1) // usePoint에서 -1이 입력되면 뒤로가기로 체크
					continue;

				appView.printNotice(String.format("판매액은 %,d원입니다. 받으신 금액을 입력해 주세요.\n", cart.getFinalPrice()));
				receivedCash = appView.inputInt();

				appView.printNotice(String.format("받으신 금액은 %,d원이며, 거스름돈은 %,d원입니다.\n", receivedCash,
						(receivedCash - cart.getFinalPrice())));

				appView.printNotice("금액을 정산하신 후 엔터 버튼을 눌러주세요.");
				appView.inputEnter();
				
				appView.printNotice("현금영수증 여부를 선택해 주세요.");
				
				plView = new PrintLineView();
				plView.addView("(1) 사업자 증빙용");
				plView.addView("(2) 개인용");
				plView.addView("(0) 건너뛰기");
				plView.printList();
				
				int receiptType = 0;
				boolean isComplete = false;
				while (!isComplete) {
					receiptType = appView.inputInt();
					
					switch(receiptType) {
					case 1:
						//사업자 번호 입력 받음(10자리)
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
						//아무 동작 하지 않음
					}
				}
			}
			
			break;
		}
		
		System.out.println("\n\n");
		
		this.printReceipt(purchaseType, receivedCash, taxNumber);
	}
	
	@Override
	protected void addCart() {
		while (true) {
			Product product;
			Coupon coupon;
			appView.printNotice("물품이나 쿠폰의 코드 번호를 입력해 주세요. (종료는 0 입력)");

			int input = appView.inputInt();
			int inputCount = 0;

			if (input == 0)
				return;
			
			coupon = CouponHelper.getCoupon(input); //우선 입력된 코드를 쿠폰 데이터베이스에서 검색
			
			if(coupon != null) { //쿠폰일 때
				if(coupon.getIsUsed()) {
					appView.printError("이미 사용된 쿠폰입니다.");
					continue;
				}
				product = ProductHelper.getProduct(coupon.getProductCode());
				product.setProductCount(coupon.getProductCount());
				inputCount = coupon.getProductCount();
				
				int allPrice = product.getProductPrice() * product.getProductCount();
				int discountPrice = (int)(allPrice * (coupon.getDiscountRate() / 100.0));
				cart.setDiscountPrice(cart.getDiscountPrice() + discountPrice);
				
				coupon.setIsUsed(true);
				CouponHelper.updateCoupon(coupon);
				
			} else { //쿠폰의 번호가 아니면 물품 번호에서 검색함
				product = ProductHelper.getProduct(input);
				
				if (product == null) { //물품 코드도 아니므로
					appView.printError("올바른 코드 번호가 아닙니다.");
					continue;
				}

				appView.printNotice("물품의 개수를 입력해 주세요. (종료는 0 입력)");

				inputCount = appView.inputInt();

				if (inputCount == 0) {
					product = null;
					appView.printNotice("물품을 추가하지 않았습니다.");
					break;
				}
			}

			if(product.getProductRemain() < inputCount) {
				product = null;
				appView.printNotice("재고수량보다 물품을 많이 구매할 수 없습니다.");
				continue;
			}

			product.setProductCount(inputCount); //product 객체에 카트에 담은 물품의 개수 추가

			if (product != null)
				this.cart.addProduct(product);
			
			appView.printMessage("\n\n\n");
			
			this.printAllProduct();
			System.out.println();
		}
	}

	protected void printAllProduct() {
		Collection<Product> products = this.cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		int index = 1;
		ReceiptView receiptView = new ReceiptView();

		receiptView.printLine();
		receiptView.printHead();
		receiptView.printLine();
		while (ite.hasNext()) {
			Product tempProduct = ite.next();
			receiptView.setData(index, tempProduct.getProductName(), tempProduct.getProductCount(),
					tempProduct.getProductPrice());
			receiptView.printBody();
			index++;
		}
		receiptView.printLine();
		receiptView.printReceiptPrice(this.cart.getFinalPrice());
		receiptView.printLine();
	}

	private void printReceipt(int purchaseType, int receivedCash, String taxNumber) {
		Collection<Product> products = this.cart.getAllProduct();
		Iterator<Product> ite = products.iterator();
		int index = 1;
		ReceiptView receiptView = new ReceiptView();

		receiptView.printLine();
		receiptView.printHead();
		receiptView.printLine();
		
		//계산 목록 시작
		while (ite.hasNext()) {
			Product tempProduct = ite.next();
			receiptView.setData(index, tempProduct.getProductName(), tempProduct.getProductCount(),
					tempProduct.getProductPrice());
			receiptView.printBody();
		}
		//계산 목록 끝
		
		//판매액 및 결제방식 시작
		receiptView.printLine();
		receiptView.printDiscountPrice(this.cart.getDiscountPrice());
		receiptView.printReceiptPrice(this.cart.getFinalPrice());
		if (purchaseType == 1) {
			receiptView.printPurchageType(Enums.PURCHASE_CARD);
		} else if (purchaseType == 2) {
			receiptView.printPurchageType(Enums.PURCHASE_CASH);
		}
		//판매액 및 결제방식 끝
		
		//받은금액 시작
		receiptView.printLine();
		receiptView.printCashData(receivedCash, this.cart.getFinalPrice());
		receiptView.printLine();
		//받은금액 끝
		
		//현금영수증 시작
		if(taxNumber != null) {
			if (taxNumber.length() == 10) { // 사업자 증빙용
				receiptView.printTaxNumberForBuisness(taxNumber);
				receiptView.printLine();
			} else if (taxNumber.length() == 11) { // 개인용
				receiptView.printTaxNumberForNormal(taxNumber);
				receiptView.printLine();
			}
		}
		//현금영수증 끝

		appView.printMessage("");
	}
	
	private int usePoint() {
		appView.printNotice("포인트를 적립 또는 사용하시겠습니까?");
		
		plView = new PrintLineView();
		plView.addView("(1) 적립");
		plView.addView("(2) 사용");
		plView.addView("(9) 뒤로가기");
		plView.addView("(0) 아니요");
		plView.printList();

		int pointType = 0;
		while (true) {
			pointType = appView.inputInt();
			if (pointType == 1 || pointType == 2 || pointType == 0) {
				break;
			}
			if(pointType == 9) return -1;
			appView.printError("올바른 번호가 아닙니다.");
		}
		
		if(pointType == 1) {
			appView.printNotice("포인트를 적립합니다.");
			int pointCardNumber = 0;
			Point point;
			while(true) {
				appView.printNotice("사용자의 포인트 카드 번호를 입력해 주세요. (뒤로 가기는 0 입력)");
				pointCardNumber = appView.inputInt();
				if(pointCardNumber == 0) return -1;
				point = PointHelper.getPoint(pointCardNumber); //PointHelper에서 Point 객체를 불러옴
				if(point == null) {
					appView.printError("해당하는 포인트 카드 번호가 없습니다.");
					continue;
				}
				break;
			}
			int addPoint = (int)(((double) this.cart.getCartPrice()) * POINT_RATE);
			point.setUserPoint(point.getUserPoint() + addPoint);
			PointHelper.updatePoint(point);
			appView.printNotice(String.format("%,d 포인트가 적립되었습니다.", addPoint));
		} else if(pointType == 2) {
			appView.printNotice("포인트를 사용합니다.");
			int pointCardNumber = 0;
			Point point;
			while(true) {
				appView.printNotice("사용자의 포인트 카드 번호를 입력해 주세요. (뒤로 가기는 0 입력)");
				pointCardNumber = appView.inputInt();
				if(pointCardNumber == 0) return -1;
				point = PointHelper.getPoint(pointCardNumber);
				if(point == null) {
					appView.printError("해당하는 포인트 카드 번호가 없습니다.");
					continue;
				}
				break;
			}
			
			appView.printNotice(String.format("포인트 잔액은 %,d원입니다. 사용할 포인트를 입력해 주세요.", point.getUserPoint()));
			int usePoint = 0;
			while(true) {
				usePoint = appView.inputInt();
				if(usePoint > point.getUserPoint()) {
					appView.printNotice("사용할 포인트가 부족합니다.");
					continue;
				} else if(usePoint > this.cart.getFinalPrice()) {
					appView.printNotice("결제 금액보다 포인트를 많이 사용할 수 없습니다.");
					continue;
				}
				break;
			}
			point.setUserPoint(point.getUserPoint() - usePoint);
			PointHelper.updatePoint(point);
			this.cart.setDiscountPrice(this.cart.getDiscountPrice() + usePoint);
			appView.printNotice(String.format("%,d원이 할인되었습니다.", usePoint));
			return 0;
		}
		return 0;
	}
	

	@Override
	protected void printHead() {
		appView.printNotice("물품 계산 기능입니다.");
	}
}
