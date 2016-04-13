package server.screenLib;

import javafx.scene.image.ImageView;
import library.Conversions;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ScreenView extends Thread {

    private Socket socket;
    private OnAcceptInterface acceptEvent;
    private ImageView view;

    public ScreenView(Socket socket, OnAcceptInterface event){
        this.socket = socket;
        this.acceptEvent = event;
        view = new ImageView();
    }

    @Override
    public void run() {
        DataInputStream inputStream = null;
        int length;
        try {
            inputStream = this.initInput();
            view = acceptEvent.createView();
            while ((length=inputStream.readInt())!=-1){
                acceptEvent.onReceive( Conversions.byteArrayToImage(length, inputStream) , view );
            }
        }catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
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

}
