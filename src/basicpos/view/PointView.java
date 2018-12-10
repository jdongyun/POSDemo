package basicpos.view;

public class PointView implements PrintView {
	private final static char WALL = '-';
	private int userCode;
	private int userPoint;
	
	public PointView() {
		
	}
	
	public void printHead() {
		System.out.println("고객 포인트 코드     잔액 정보");
	}
	
	public void printLine() {
		int printCount = 30; //Default print count
		for(int i = 0; i < printCount; i++) {
			System.out.print(WALL);
		}
		System.out.println();
	}
	
	public void printBody() {
		System.out.println(String.format("%d   %,24d", userCode, userPoint));
	}
	
	public void setBody(int userCode, int userPoint) {
		this.userCode = userCode;
		this.userPoint = userPoint;
	}
}
