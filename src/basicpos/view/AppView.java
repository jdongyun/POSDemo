package basicpos.view;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppView {
	
	private final static Scanner scanner = new Scanner(System.in);
	
	public void printNotice(String message) {
		System.out.println("** " + message);
	}
	
	public void printError(String message) {
		System.out.println("## " + message);
	}
	
	public void printMessage(String message) {
		System.out.println(message);
	}
	
	public int inputInt() {
		String input;
		int inputInt = 0;
		while(true) {
			System.out.print("입력 : ");
			try {
				input = scanner.nextLine();
				
				if(! input.equals("")) {
					inputInt = Integer.parseInt(input);
					break;
				}
				
			} catch (NumberFormatException e) {
				System.out.println("정상적인 값이 입력되지 않았습니다.");
			}
		}
		//if(input.equals("")) 
		return inputInt;
	}
	
	public String inputString() {
		System.out.print("입력 : ");
		String input = scanner.nextLine();
		return input;
	}
	
	public void clearInputBuffer() {
		scanner.nextLine();
	}
	
}
