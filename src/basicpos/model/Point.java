package basicpos.model;

public class Point {
	private int userCode;
	private int userPoint;
	
	public Point(int userCode, int userPoint) {
		this.userCode = userCode;
		this.userPoint = userPoint;
	}
	
	public int getUserCode() {
		return this.userCode;
	}
	
	public int getUserPoint() {
		return this.userPoint;
	}
}