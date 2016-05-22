package client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

public class ScreenViewClient {
	
	private Timer timer;
    private Socket socket;
    private ServerSocket litener;
    private SendScreenTask task;
	
	public ScreenViewClient(String name, int portNmb){
        try {
            socket = new Socket(name, portNmb);
        }catch( Exception e){e.printStackTrace();}
        System.out.println("[Client] Timer started");
	}

    public ScreenViewClient(int portNmb){
        try {
            litener = new ServerSocket(portNmb+100);
        }catch( Exception e){e.printStackTrace();}
    }
	
	public void sendingSmallScreen(){
    	timer = new Timer();
        task = new SendScreenTask( socket, timer, 4 );
        timer.schedule(task, 0, 10);
    }

    public void stopSmallClient(){
        timer.cancel();
        task.closeConn();
        System.out.println("[Client] Timer closed");
    }

    public void stopMaxClient(){
        try{
            litener.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("[Client] Max listener closed");
    }

    public void listenForMaximized(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("[CLIENT]" + Thread.currentThread().getName() + " started");
                try{
                    System.out.println("[CLIENT] listen for maximize request");
                    socket = litener.accept();
                    System.out.println("[CLIENT] Maximize request");
                    timer = new Timer();
                    SendScreenTask task = new SendScreenTask( socket, timer, 1.5 );
                    timer.schedule(task, 0, 10);
                    stopMaxClient();
                    System.out.println("[CLIENT]" + Thread.currentThread().getName() + " closed");
                }catch (Exception e){e.printStackTrace();}
            }
        }).start();
    }
}