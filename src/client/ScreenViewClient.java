package client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

public class ScreenViewClient {
	
	private Timer timer;
    private Socket socket;
    private ServerSocket litener;
	
	public ScreenViewClient(String name, int portNmb){
        try {
            socket = new Socket(name, portNmb);
        }catch( Exception e){e.printStackTrace();}
	}

    public ScreenViewClient(int portNmb){
        try {
            litener = new ServerSocket(portNmb);
        }catch( Exception e){e.printStackTrace();}
    }
	
	public void sendingSmallScreen(){
    	timer = new Timer();
        SendScreenTask task = new SendScreenTask( socket, timer, 4 );
        timer.schedule(task, 0, 10);
    }

    public void listenForMaximized(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = litener.accept();
                }catch (Exception e){e.printStackTrace();}
                System.out.println("[IMAGE CLIENT] Maximize request");
                timer = new Timer();
                SendScreenTask task = new SendScreenTask( socket, timer, 1.5 );
                timer.schedule(task, 0, 10);
            }
        }).start();
    }
}