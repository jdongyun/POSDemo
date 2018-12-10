package basicpos.view;

public class PrintLineView extends MainView {

	private void printData(String str) {
		System.out.print(str);
	}
	
	@Override
	public void printList() {
		int size = list.size();
		int ite = 0;
		
		while(ite < size) { //size에서 홀수만큼 뺌
			String str = list.get(ite);
			if(ite != 0) {
				System.out.print("   ");
			}
			printData(str);
			ite++;
		}
		System.out.println();
	}
}
