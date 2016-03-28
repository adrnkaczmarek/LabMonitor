package stream;

public class Main {
	
    public static void main(String[] args) {
    	Streamer stream = new Streamer("localhost", 11937);
    	stream.startTimer();
    }    
}