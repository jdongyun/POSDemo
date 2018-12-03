package basicpos.controller;

import java.util.Iterator;

import basicpos.model.DBHelper;
import basicpos.model.Point;
import basicpos.model.PointHelper;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.MainView;
import basicpos.view.PointView;
import basicpos.view.ReceiptView;

public class ManageController {
	private AppView appView;
	private MainView mainView;
	
	public ManageController() {
		this.appView = new AppView();
		this.mainView = new MainView();
	}
	
	public void run() {
		mainView.addView("1. 물품 목록");
		mainView.addView("2. 고객 포인트 정보");
		mainView.addView("3. 물품 추가");
		mainView.addView("4. 물품 제거");
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
			case 2:
				appView.printNotice("고객들의 포인트 정보를 출력합니다.");
				this.printPointData();
				break;
			case 3: //물품 추가
				appView.printNotice("물품을 추가합니다.");
				this.insertProduct();
				break;
			case 4: //물품 추가
				appView.printNotice("물품을 제거합니다.");
				this.deleteProduct();
				break;
			case 0:
				appView.printNotice("메인 메뉴로 이동합니다.");
				return;
			default:
				appView.printError("다시 입력해 주십시오.");
			}
		}
	}
	
	private void printProduct() {
		ReceiptView receiptView = new ReceiptView(ReceiptView.Print.PRINT_INFO);
		Iterator<Product> ite = ProductHelper.getProductList();
		
		appView.printMessage("");
		receiptView.printLine();
		receiptView.printHead();
		receiptView.printLine();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			
			receiptView.setReceiptProduct(tempProduct.getProductCode(), 
					tempProduct.getProductName(), 
					tempProduct.getProductPrice());
			receiptView.printBody();
		}
		receiptView.printLine();
		appView.printMessage("");
	}
	
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
	
	private void insertProduct() {
		int productCode = 0;
		String productName = "";
		int productPrice = 0;
		boolean isAdultOnly = false;

		appView.printNotice("추가할 물품의 코드를 입력해 주세요.");
		productCode = appView.inputInt();
		
		appView.printNotice("추가할 물품의 이름을 입력해 주세요.");
		productName = appView.inputString();
		
		appView.printNotice("추가할 물품의 가격을 입력해 주세요.");
		productPrice = appView.inputInt();
		
		while(true) {
			appView.printNotice("추가할 물품이 나이 확인이 필요한지 입력해 주세요. (y/n)");
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
		
		if(ProductHelper.insertProduct(productCode, productName, productPrice, isAdultOnly)) {
			appView.printNotice("물품 추가가 완료되었습니다.");
		} else {
			appView.printError("물품 추가 중 문제가 발생하였습니다.");
		}
	}
	
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
}
