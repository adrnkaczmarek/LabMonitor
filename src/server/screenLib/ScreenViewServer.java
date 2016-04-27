package server.screenLib;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ScreenViewServer implements Runnable {

    private ServerSocket server;
    private Socket socket;
    private OnAcceptInterface acceptEvent;

    public ScreenViewServer(int port, OnAcceptInterface acceptEvent){
    	try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.acceptEvent = acceptEvent;
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.socket = this.server.accept();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socket.close();
                    server.close();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            new ScreenView(this.socket, this.acceptEvent, new ImageView()).start();
        }
    }
}