package basicpos.controller;

import java.util.Collection;
import java.util.Iterator;

import basicpos.dao.CalcDao;
import basicpos.model.PointHelper;
import basicpos.model.Product;
import basicpos.view.ReceiptView;

public class PurchaseController extends CalcDao {
	private static double POINT_RATE = 0.01;
	
	@Override
	protected void pay() {

		this.printAllProduct();

		System.out.println("\n");

		if (cart.hasAdultProduct()) {
			appView.printNotice("나이를 확인해야 하는 물품이 있습니다.\n");
		}
		
		int purchaseType = 0;
		int receivedCash = this.cart.getAllPrice(); // 카드결제일 때의 기본값은 결제금액.
		int discountPrice = 0;
		String taxNumber = null;

		while(true) {
			
			
			appView.printNotice("결제 수단을 선택해 주세요.");
			appView.printMessage("(1) 신용카드     (2) 현금     (0) 종료");
			
			
			while (true) {
				purchaseType = appView.inputInt();
				if (purchaseType == 1 || purchaseType == 2|| purchaseType == 3) {
					break;
				}
				if (purchaseType == 0) {
					return;
				}
				appView.printError("올바른 번호가 아닙니다.");
			}
			
			if(purchaseType == 1) {
				
				appView.printNotice("신용카드 결제입니다.");
				
				discountPrice = usePoint();
				if(discountPrice == -1) //usePoint에서 -1이 입력되면 뒤로가기로 체크
					continue;
				
				appView.printNotice("신용카드 번호를 입력해 주세요. (뒤로 가기는 0 입력)");
				String cardNumber = appView.inputString();
				if(cardNumber.equals("0")) continue;
				
				appView.printNotice("결제가 완료되었습니다.");
				
			} else if (purchaseType == 2) {
				appView.printNotice("현금 결제입니다.");
				
				discountPrice = usePoint();
				if(discountPrice == -1) //usePoint에서 -1이 입력되면 뒤로가기로 체크
					continue;
				
				appView.printNotice(String.format("판매액은 %,d원입니다. 받으신 금액을 입력해 주세요.\n", cart.getAllPrice() - discountPrice));
				receivedCash = appView.inputInt();
				
				appView.printNotice(String.format("받으신 금액은 %,d원이며, 거스름돈은 %,d원입니다.\n", receivedCash,
						(receivedCash - (cart.getAllPrice() - discountPrice))));
				
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
			
			
			
			break;
		}
		
		System.out.println("\n\n");

		this.printReceipt(purchaseType, receivedCash, discountPrice, taxNumber);

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

	private void printReceipt(int purchaseType, int receivedCash, int discountPrice, String taxNumber) {
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
		receiptView.printDiscountPrice(discountPrice);
		receiptView.printReceiptPrice(this.cart.getAllPrice() - discountPrice);
		if (purchaseType == 1) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CARD);
		} else if (purchaseType == 2) {
			receiptView.printPurchageType(ReceiptView.PurchaseType.PURCHASE_CASH);
		}
		//판매액 및 결제방식 끝
		
		//받은금액 시작
		receiptView.printReceiptLine();
		receiptView.printCashData(receivedCash, this.cart.getAllPrice() - discountPrice);
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
	
	private int usePoint() {
		appView.printNotice("포인트를 적립 또는 사용하시겠습니까?");
		appView.printMessage("(1) 적립     (2) 사용     (9) 뒤로 가기     (0) 아니요");
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
			Integer userPoint;
			while(true) {
				appView.printNotice("사용자의 포인트 카드 번호를 입력해 주세요. (뒤로 가기는 0 입력)");
				pointCardNumber = appView.inputInt();
				if(pointCardNumber == 0) return -1;
				userPoint = PointHelper.getPoint(pointCardNumber);
				if(userPoint == null) {
					appView.printError("해당하는 포인트 카드 번호가 없습니다.");
					continue;
				}
				break;
			}
			int addPoint = (int)(((double) this.cart.getAllPrice()) * POINT_RATE);
			PointHelper.setPoint(pointCardNumber, userPoint + addPoint);
			appView.printNotice(String.format("%,d 포인트가 적립되었습니다.", addPoint));
		} else if(pointType == 2) {
			appView.printNotice("포인트를 사용합니다.");
			int pointCardNumber = 0;
			Integer userPoint;
			while(true) {
				appView.printNotice("사용자의 포인트 카드 번호를 입력해 주세요. (뒤로 가기는 0 입력)");
				pointCardNumber = appView.inputInt();
				if(pointCardNumber == 0) return -1;
				userPoint = PointHelper.getPoint(pointCardNumber);
				if(userPoint == null) {
					appView.printError("해당하는 포인트 카드 번호가 없습니다.");
					continue;
				}
				break;
			}
			
			appView.printNotice(String.format("포인트 잔액은 %,d원입니다. 사용할 포인트를 입력해 주세요.", userPoint));
			int usePoint = 0;
			while(true) {
				usePoint = appView.inputInt();
				if(usePoint > userPoint) {
					appView.printNotice("사용할 포인트가 부족합니다.");
					continue;
				} else if(usePoint > this.cart.getAllPrice()) {
					appView.printNotice("결제 금액보다 포인트를 많이 사용할 수 없습니다.");
					continue;
				}
				break;
			}
			PointHelper.setPoint(pointCardNumber, userPoint - usePoint);
			appView.printNotice(String.format("%,d원이 할인되었습니다.", usePoint));
			return usePoint;
		}
		return 0;
	}
	

	@Override
	protected void printHead() {
		appView.printNotice("물품 계산 기능입니다.");
	}
}
