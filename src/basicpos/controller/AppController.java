package basicpos.controller;
import java.util.Collection;
import java.util.Iterator;

import basicpos.model.Cart;
import basicpos.model.Product;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.MainView;
import basicpos.view.ReceiptView;

public class AppController {
	private AppView appView;
	private MainView mainView;
	
	public AppController() {
		this.appView = new AppView();
		this.mainView = new MainView();
	}
	
	public void run() {
		
		mainView.addView("1. 물건 계산");
		mainView.addView("2. 물건 목록 확인");
		mainView.addView("3. 관리 페이지");
		mainView.addView("0. 종료");

		while(true) {
			mainView.printList();
			
			int input = appView.inputInt();
			
			switch(input) {
			case 1:
				PurchaseController pc = new PurchaseController();
				pc.purchase();
				break;
			case 0:
				appView.printNotice("프로그램을 종료합니다.");
				System.exit(0);
			default:
				appView.printError("다시 입력해 주십시오.");
			}
		}
		
	}
	
}
