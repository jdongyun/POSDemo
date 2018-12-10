package basicpos.controller;

import basicpos.dao.CalcDao;
import basicpos.model.ProductHelper;
import basicpos.view.AppView;
import basicpos.view.MainView;

public class AppController {
	private AppView appView;
	private MainView mainView;
	
	public AppController() {
		this.appView = new AppView();
		this.mainView = new MainView();
	}
	
	public void run() {
		mainView.addView("1. 물품 계산");
		mainView.addView("2. 물품 환불");
		mainView.addView("3. 관리자 페이지");
		mainView.addView("0. 종료");

		while(true) {
			mainView.printList();
			int input = appView.inputInt();
			
			switch(input) {
			case 1: //물품 계산
				CalcDao pc = new PurchaseController();
				pc.run();
				break;
			case 2: //물품 환불
				CalcDao rc = new RefundController();
				rc.run();
				break;
			case 3: //물품 목록 확인 
				ManageController mc = new ManageController();
				mc.run();
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
