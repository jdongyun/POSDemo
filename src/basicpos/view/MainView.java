package basicpos.view;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import basicpos.Enums;

public class MainView {
	private final static char WALL = '*';

	protected List<String> list;
	
	public MainView() {
		list = new ArrayList<String>();
	}
	
	public void addView(String str) {
		list.add(str);
	}
	
	private int getLength(String str) {
		try {
			byte[] bytes = str.getBytes("ms949");
			return bytes.length;
		} catch (UnsupportedEncodingException e){
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	private void printWall() {
		//WALL 문자를 한 번 출력한다. (줄바꿈은 하지 않는다.)
		System.out.print(WALL);
	}
	
	private void printWallLine() {
		//WALL 문자를 50번 출력한다.
		for(int i = 0; i < 50; i++)
			printWall();
		System.out.println();
	}
	
	private void printMargin(Enums e) {
		printWall();
		for(int i = 0; i < 48; i++) {
			if(i == 23 && e == Enums.MARGIN_TWO) {
				printWall();
				continue;
			}
			System.out.print(" ");
		}
		printWall();
		System.out.println();
	}
	
	private void printData(String str1, String str2) {
		//한 줄에 두 가지 정보가 출력되도록 한다.
		printWall();
		System.out.print("  "); //벽과 두 칸 띄움
		System.out.print(str1);
		
		//빈칸 출력
		for(int i = 0; i < (21 - getLength(str1)); i++)
			System.out.print(" ");
		
		printWall();
		System.out.print("  "); //벽과 두 칸 띄움
		System.out.print(str2);
		
		//빈칸 출력
		for(int i = 0; i < (22 - getLength(str2)); i++)
			System.out.print(" ");
		printWall();
		System.out.println();
	}
	
	private void printData(String str) {
		printWall();
		System.out.print("  "); //벽과 두 칸 띄움
		System.out.print(str);
		
		for(int i = 0; i < (46 - getLength(str)); i++)
			System.out.print(" ");
		
		printWall();
		System.out.println();
	}
	
	public void printList() {
		int size = list.size();
		int ite = 0;
		
		printWallLine();
		
		while(ite < (size - (size % 2))) { //size에서 홀수만큼 뺌
			String str1 = list.get(ite);
			String str2 = list.get(ite + 1);
			
			printMargin(Enums.MARGIN_TWO);
			printData(str1, str2);
			printMargin(Enums.MARGIN_TWO);
			printWallLine();
			ite = ite + 2;
		}
		
		if(size % 2 == 1) {
			String str = list.get(ite);
			printMargin(Enums.MARGIN_ONE);
			printData(str);
			printMargin(Enums.MARGIN_ONE);
			printWallLine();
		}

	}
	
	
}
