package basicpos.view;

public interface PrintView {
	public void printHead(); //목록 최상위에 뜨는 제목들을 출력하는 메소드
	public void printLine(); //제목과 내용을 구분하는 구분자를 출력하는 메소드
	public void printBody(); //내용을 출력하는 메소드
}
