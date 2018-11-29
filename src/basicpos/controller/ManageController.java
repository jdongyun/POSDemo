package basicpos.controller;

import java.util.Iterator;

import basicpos.model.DBHelper;
import basicpos.model.Point;
import basicpos.model.PointHelper;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.MainView;
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
				appView.printNotice("고객 포인트 정보를 출력합니다.");
				this.printPointData();
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
		ProductHelper pHelper = new ProductHelper();
		Iterator<Product> ite = pHelper.getProductDB();
		
		appView.printMessage("");
		receiptView.printReceiptLine();
		receiptView.printReceiptHead();
		receiptView.printReceiptLine();
		while(ite.hasNext()) {
			Product tempProduct = ite.next();
			
			receiptView.printReceiptProduct(tempProduct.getProductCode(), 
					tempProduct.getProductName(), 
					tempProduct.getProductPrice());
		}
		receiptView.printReceiptLine();
		appView.printMessage("");
	}
	
	private void printPointData() {
		PointHelper pHelper = new PointHelper();
		Iterator<Point> ite = pHelper.getPointDB();
		
		appView.printMessage("");
		appView.printMessage("고객 포인트 코드     잔액 정보");
		while(ite.hasNext()) {
			Point tempPoint = ite.next();
			System.out.println(tempPoint);

		}
		appView.printMessage("");
		DBHelper.close();
	}
}
