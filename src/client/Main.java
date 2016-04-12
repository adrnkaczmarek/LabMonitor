package client;

public class Main {
	
    public static void main(String[] args) {
    	ScreenViewClient streamScreen = new ScreenViewClient("localhost", 11937);
    	streamScreen.startTimer();
    }    
}