package client;

import java.net.Socket;
import java.util.Timer;

public class ScreenViewClient {
	
	private Timer timer;
    private Socket socket;
	
	public ScreenViewClient(String name, int portNmb){
        try {
            socket = new Socket(name, portNmb);
        }catch( Exception e){e.printStackTrace();}
	}
	
	public void startTimer(){
    	timer = new Timer();
        SendScreenTask task = new SendScreenTask( socket, timer, 4 );
        timer.schedule(task, 0, 10);
    }
}