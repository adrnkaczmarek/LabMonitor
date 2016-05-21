package server.screenLib;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ScreenViewServer implements Runnable {

    private static ServerSocket server;
    private OnAcceptInterface acceptEvent;
    public static boolean isRunning = true;

    public ScreenViewServer(int port, OnAcceptInterface acceptEvent){
    	try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.acceptEvent = acceptEvent;
    }

    public static void closeServer(){
        try{
            server.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("[Server] " + Thread.currentThread().getName() + " started");
        while(isRunning) {
            try {
                Socket socket = this.server.accept();
                new ScreenView(socket, this.acceptEvent, new ImageView()).start();
            } catch (Exception e) {
                e.printStackTrace();
                closeServer();
            }
        }
        System.out.println("[Server] " + Thread.currentThread().getName() + " closed");
    }
}