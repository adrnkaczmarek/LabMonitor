package server;

import library.Conversions;
import server.window_app.OnAcceptInterface;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ScreenViewServer extends Thread {

    private ServerSocket server;
    private Socket socket;
    private OnAcceptInterface acceptEvent;

    public ScreenViewServer(int port_number,OnAcceptInterface acceptEvent){
    	try {
			this.server = new ServerSocket(port_number);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.acceptEvent = acceptEvent;
    }
    
    
    private DataInputStream initInput(){
    	InputStream input_socket = null;
    	BufferedInputStream input_buffer = null;
		try {
			input_socket = this.socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		input_buffer = new BufferedInputStream( input_socket );
		return new DataInputStream ( input_buffer );
	}

    @Override
    public void run() {
        DataInputStream inputStream = null;
        int length;
		try {
            this.socket = this.server.accept();
            inputStream = this.initInput();

            while ((length=inputStream.readInt())!=-1){
                acceptEvent.onReceive( Conversions.byteArrayToImage(length, inputStream) );
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
                server.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}