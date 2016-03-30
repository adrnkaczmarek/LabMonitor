package stream;

public class Main {
	
    public static void main(String[] args) {
    	ScreenViewClient stream = new ScreenViewClient("localhost", 11937);
    	stream.startTimer();
    }    
}